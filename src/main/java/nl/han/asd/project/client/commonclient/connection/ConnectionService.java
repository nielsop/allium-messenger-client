package nl.han.asd.project.client.commonclient.connection;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;

import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper.Builder;
import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper.Type;

/**
 * {@link IConnectionService} implementation.
 *
 * <p>
 * This implementation aims to restrict the concurrent number
 * of open socket connections within a single instance of itself.
 *
 * <p>
 * This functionally can however be overwritten for time-critical
 * messages using the {@link IConnectionService#write(Wrapper, long, TimeUnit)}
 * and {@link IConnectionService#writeAndRead(Wrapper, long, TimeUnit)} for writing
 * without reading and writing while expecting a response respectively.
 *
 * @version 1.0
 */
public class ConnectionService implements IConnectionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionService.class);

    private Semaphore mutex;

    private IEncryptionService encryptionService;

    private String host;
    private int port;

    private SocketHandler socketHandler;

    private byte[] publicKeyBytes;

    /**
     * Create a new ConnectionService instance used
     * to handle the communication with the specified host.
     *
     * <p>
     * Note that this method does not check the validity
     * of the provided hostname or port.
     *
     * @param host to-be-conencted to host
     * @param port port to use during connection
     *
     * @throws IllegalArgumentException if host is null
     */
    @AssistedInject
    public ConnectionService(@Assisted String host, @Assisted int port) {
        this.host = Check.notNull(host, "host");
        this.port = port;

        encryptionService = null;

        mutex = new Semaphore(1);
    }

    /**
     * Create a new ConnectionService instance used
     * to handle the communication with the specified host.
     *
     * <p>
     * Note that this method does not check the validity
     * of the provided hostname or port.
     *
     * @param encryptionService service used to en-/decrypt messages
     * @param host to-be-connected to host
     * @param port port to use during connection
     * @param publicKeyBytes public key of the to-be-connected to host
     *
     * @throws IllegalArgumentException if encryptionService, host
     *          and/or publicKeyBytes is null
     */
    @AssistedInject
    public ConnectionService(IEncryptionService encryptionService, @Assisted String host, @Assisted int port,
            @Assisted byte[] publicKeyBytes) {
        this(host, port);

        this.encryptionService = Check.notNull(encryptionService, "encryptionService");
        this.publicKeyBytes = Check.notNull(publicKeyBytes, "publicKeyBytes");
    }

    /**
     * Create a new ConnectionService instance used
     * to handle the communication with the specified host.
     *
     * <p>
     * Note that this method does not check the validity
     * of the provided hostname or port.
     *
     * <p>
     * The functionally provided by this constructor is
     * equal to:
     *
     * <pre>
     *  IEncryptionService encryptionService = ...;
     *  String host = ...;
     *  int port = ...;
     *
     *  byte[] publicKeyBytes = null;
     *  File publicKeyFile = new File(publicKeyFilePath);
     *
     *  try (FileInputStream fileInputStream = new FileInputStream(publicKeyFile)) {
     *      DataInputStream dataInputStream = new DataInputStream(fileInputStream);
     *      publicKeyBytes = new byte[(int) publicKeyFile.length()];
     *      dataInputStream.readFully(publicKeyBytes);
     *  }
     *
     *  new ConnectionService(encryptionService, host, port, publicKeyBytes);
     * </pre>
     *
     * @param encryptionService service used to en-/decrypt messages
     * @param host to-be-connected to host
     * @param port port to use during connection
     * @param publicKeyFile byte file containing the hosts public key
     *
     * @throws IllegalArgumentException if encryptionService, host
     *          and/or publicKeyBytes is null
     * @throws IOException if an IOException occurs during the publicKeyFile read
     */
    @AssistedInject
    public ConnectionService(IEncryptionService encryptionService, @Assisted String host, @Assisted int port,
            @Assisted File publicKeyFile) throws IOException {
        this(host, port);
        this.encryptionService = Check.notNull(encryptionService, "encryptionService");

        Check.notNull(publicKeyFile, "publicKeyFile");
        try (FileInputStream fileInputStream = new FileInputStream(publicKeyFile)) {
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);

            publicKeyBytes = new byte[(int) publicKeyFile.length()];
            dataInputStream.readFully(publicKeyBytes);
        }
    }

    /** {@inheritDoc} */
    @Override
    public <T extends GeneratedMessage> Wrapper wrap(T message, Type type) {
        Check.notNull(message, "message");
        Check.notNull(type, "type");

        Builder wrapperBuilder = Wrapper.newBuilder();
        wrapperBuilder.setType(type);

        if (encryptionService != null) {
            byte[] encryptedMessage = encryptionService.encryptData(publicKeyBytes, message.toByteArray());
            wrapperBuilder.setData(ByteString.copyFrom(encryptedMessage));
        } else {
            wrapperBuilder.setData(message.toByteString());
        }

        return wrapperBuilder.build();
    }

    private void writeNewSocket(Wrapper wrapper) {
        try (SocketHandler newSocketHandler = new SocketHandler(host, port)) {
            newSocketHandler.write(wrapper);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void write(Wrapper wrapper, long timeout, TimeUnit unit)
            throws IOException, MessageNotSentException {
        Check.notNull(wrapper, "wrapper");

        try {
            if (mutex.tryAcquire(timeout, unit)) {
                try {
                    socketHandler.write(wrapper);
                } finally {
                    if (!mutex.hasQueuedThreads()) {
                        socketHandler.close();
                    }

                    mutex.release();
                }
            } else {
                writeNewSocket(wrapper);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().isInterrupted();
            throw new MessageNotSentException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void write(Wrapper wrapper) throws IOException, MessageNotSentException {
        write(wrapper, -1, TimeUnit.MILLISECONDS);
    }

    private GeneratedMessage writeAndReadNewSocket(Wrapper wrapper)
            throws InterruptedException, IOException {
        try (SocketHandler socketHandler = new SocketHandler(host, port, encryptionService)) {
            return socketHandler.writeAndRead(wrapper);
        } finally {
            socketHandler.close();
        }
    }

    /** {@inheritDoc} */
    @Override
    public GeneratedMessage writeAndRead(Wrapper wrapper, long timeout, TimeUnit unit)
            throws IOException, MessageNotSentException {
        Check.notNull(wrapper, "wrapper");

        try {
            if (mutex.tryAcquire(timeout, unit)) {
                try {
                    return socketHandler.writeAndRead(wrapper);
                } finally {
                    if (!mutex.hasQueuedThreads()) {
                        socketHandler.close();
                    }

                    mutex.release();
                }
            } else {
                return writeAndReadNewSocket(wrapper);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().isInterrupted();
            throw new MessageNotSentException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public GeneratedMessage writeAndRead(Wrapper wrapper)
            throws IOException, MessageNotSentException {
        return writeAndRead(wrapper, -1, TimeUnit.MILLISECONDS);
    }
}
