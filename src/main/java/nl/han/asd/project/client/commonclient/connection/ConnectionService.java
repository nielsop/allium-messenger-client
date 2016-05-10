package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import jdk.internal.org.objectweb.asm.Handle;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.net.SocketException;

/**
 * Provides a connection service using sockets such as reading and writing data.
 *
 * @author Jevgeni Geurtsen
 */
public final class ConnectionService implements IConnectionPipe {
    private final static int DEFAULT_SLEEP_TIME = 25; // 25ms

    private Packer packer = null;
    private byte[]  receiverPublicKey = null;
    private Connection connection = null;
    private IConnectionService service = null;

    /**
     * Initializes this class.
     *
     * @param sleepTime Amount of time the asynchronous thread sleeps in between reads from the socket.
     * @param receiverPublicKey The public key of the receiver.
     */
    public ConnectionService(final int sleepTime, final byte[] receiverPublicKey) {
        connection = new Connection(this);
        Injector injector = Guice.createInjector(new EncryptionModule());
        packer = new Packer(new CryptographyService(injector.getInstance(IEncryptionService.class)));

        if (receiverPublicKey == null)
            throw new IllegalArgumentException("Public key cannot be empty.");

        this.setReceiverPublicKey(receiverPublicKey);

        if (connection.getSleepTime() != sleepTime) {
            connection.setSleepTime(sleepTime);
        }
    }

    /**
     * Initializes this class.
     * @param sleepTime Amount of time the asynchronous thread sleeps in between reads from the socket.
     * @param receiverPublicKey The public key of the receiver.
     * @param targetService An instance that implements IConnectionService. This instance will be used as callback
     *                      while reading asynchronous.
     * @throws IOException
     */
    public ConnectionService(final int sleepTime, final byte[] receiverPublicKey, final IConnectionService targetService) {
        this(sleepTime, receiverPublicKey);

        if (targetService == null) {
            throw new IllegalArgumentException("You must implement 'IServiceConnection' to your class and initialize this class with the 'this' keyword in order to use the Async read method.");
        }

        service = targetService;
    }

    /**
     * Initializes this class.
     * @param receiverPublicKey The public key of the receiver.
     */
    public ConnectionService(final byte[] receiverPublicKey) {
        this(DEFAULT_SLEEP_TIME, receiverPublicKey);
    }


    /**
     * Initializes this class.
     * @param receiverPublicKey The public key of the receiver.
     * @param targetService An instance that implements IConnectionService. This instance will be used as callback
     *                      while reading asynchronous.
     * @throws IOException
     */
    public ConnectionService(final byte[] receiverPublicKey, final IConnectionService targetService) {
        this(DEFAULT_SLEEP_TIME, receiverPublicKey, targetService);
    }

    /**
     * Reads data from the input stream.
     * @return A byte array containing the received data from the input stream, or null if no data was read.
     * @throws SocketException If there is no valid connection.
     */
    private UnpackedMessage read() throws SocketException {
        if (!connection.isConnected()) {
            throw new SocketException("Socket has no valid or connection, or the valid connection was closed.");
        }

        HanRoutingProtocol.Wrapper wrapper = connection.read();
        return packer.unpack(wrapper);
    }

    /**
     * Start reading asynchronous from the input stream.
     * @throws SocketException
     */
    public void readAsync() throws SocketException {
        if (!connection.isConnected()) {
            throw new SocketException("Socket has no valid or connection, or the valid connection was closed.");
        }
        if (service == null) {
            throw new IllegalArgumentException("You must implement 'IServiceConnection' to your class and initialize this class with the 'this' keyword in order to use the Async read method.");
        }

        // uses observer
        connection.readAsync();
    }

    /**
     * Stops reading asynchronously.
     *
     * @throws SocketException If there is no valid connection.
     */
    public void stopReadAsync() throws SocketException {
        if (!connection.isConnected()) {
            throw new SocketException("Socket has no valid or connection, or the valid connection was closed.");
        }

        connection.stopReadAsync();
    }

    /**
     * Synchronously (blocking) read a the input stream. Then converts the results into an instance of @classDescriptor.
     *
     * @param classDescriptor The class the data needs to be converted from.
     * @param <T>             Protocol buffer class.
     * @return A protocol buffer (T) instance.
     * @throws SocketException An exception occurred while reading data from the stream.
     */
    public <T extends GeneratedMessage> T readGeneric(final Class<T> classDescriptor)
            throws SocketException, InvalidProtocolBufferException,
            PackerException {
        UnpackedMessage unpackedMessage = this.read();
        if (unpackedMessage.getDataMessage().getClass() == classDescriptor)
        {
            return (T) unpackedMessage.getDataMessage().getParserForType().parseFrom(
                    unpackedMessage.getData());
        }
        return null;
    }

    /**
     * Writes data from the builder to the connection using the input stream.
     * @param instance Instance of the builder class of the protocol buffer.
     * @throws SocketException An exception occurred while writing the data.
     */
    public <T extends GeneratedMessage.Builder> void write(final T instance) throws SocketException {
        if (!connection.isConnected()) {
            throw new SocketException("Socket has no valid or connection, or the valid connection was closed.");
        }

        HanRoutingProtocol.Wrapper wrapper = packer.pack(instance, getReceiverPublicKey());
        connection.write(wrapper);
    }

    /**
     * Opens a connection to a hostname and port combination.
     *
     * @param hostName   Internet protocol address.
     * @param portNumber Port number to connect to.
     * @throws IOException If we couldn't connect to the hostname.
     */
    public void open(final String hostName, final int portNumber) throws IOException {
        connection.open(hostName, portNumber);
    }

    /**
     * Closes the existing connection.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        connection.close();
    }

    public byte[] getMyPublicKey() {
        return packer.getMyPublicKey();
    }

    /**
     * Checks whether the connection is alive or not.
     *
     * @return True if connected, False if disconnected.
     */
    public boolean isConnected() {
        return connection.isConnected();
    }

    @Override
    public void onReceiveRead(final HanRoutingProtocol.Wrapper wrapper) {
        if (service != null) {
            UnpackedMessage message = packer.unpack(wrapper);
            service.onReceiveRead(message);
        }
    }

    public void setReceiverPublicKey(byte[] receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }

    public byte[] getReceiverPublicKey() {
        return this.receiverPublicKey;
    }
}
