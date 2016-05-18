package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;

/**
 * Provides a connection service using sockets such as reading and writing data.
 *
 * @author Jevgeni Geurtsen
 */
public final class ConnectionService {
    private static final String INVALID_SOCKET_CONNECTION = "Socket has no valid or connection, or the valid connection was closed.";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionService.class);
    private Packer packer = null;
    private byte[] receiverPublicKey = null;
    private Connection connection = null;

    /**
     * Initializes this class.
     *
     * @param cryptographyService CryptographyService that should be injected.
     * @param receiverPublicKey The public key of the receiver.
     */
    @AssistedInject
    public ConnectionService(final CryptographyService cryptographyService, @Assisted final byte[] receiverPublicKey) {
        connection = new Connection();
        packer = new Packer(cryptographyService);

        if (receiverPublicKey == null)
            throw new IllegalArgumentException("Public key cannot be empty.");

        this.receiverPublicKey = receiverPublicKey;
    }

    /**
     * Reads data from the input stream.
     *
     * @return A byte array containing the received data from the input stream, or null if no data was read.
     * @throws SocketException If there is no valid connection.
     */
    private UnpackedMessage read() throws SocketException {
        if (!connection.isConnected()) {
            throw new SocketException(INVALID_SOCKET_CONNECTION);
        }

        HanRoutingProtocol.Wrapper wrapper;
        try {
            wrapper = connection.read();
        } catch (IOException e) {
            LOGGER.error("Reading from stream failed.", e);
            throw new SocketException("Could not read from stream.");
        }
        return packer.unpack(wrapper);
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
            throws SocketException, InvalidProtocolBufferException {
        UnpackedMessage unpackedMessage = this.read();
        if (unpackedMessage.getDataMessage().getClass() == classDescriptor) {
            try {
                return (T) unpackedMessage.getDataMessage().getParserForType().parseFrom(unpackedMessage.getData());
            } catch (InvalidProtocolBufferException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        throw new InvalidProtocolBufferException(String.format(
                "Received protocol type didn't match supplied parameter. Expected %s but was %s.",
                classDescriptor.getName(),
                unpackedMessage.getDataMessage().getClass().getName()));
    }

    /**
     * Writes data from the builder to the connection using the input stream.
     *
     * @param instance Instance of the builder class of the protocol buffer.
     * @throws SocketException An exception occurred while writing the data.
     */
    public <T extends GeneratedMessage.Builder> void write(final T instance) throws SocketException {
        if (!connection.isConnected()) {
            throw new SocketException(INVALID_SOCKET_CONNECTION);
        }

        HanRoutingProtocol.Wrapper wrapper = packer.pack(instance, receiverPublicKey);
        try {
            connection.write(wrapper);
        } catch (IOException e) {
            LOGGER.error("Writing to stream failed.", e);
            throw new SocketException("Could not write to stream.");
        }
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

    /**
     * Returns the public key of the cryptography instance inside the packer class.
     *
     * @return Public key
     */
    byte[] getMyPublicKey() {
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
}
