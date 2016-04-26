package nl.han.asd.project.client.commonclient.connection;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.commonservices.encryption.EncryptionService;

import java.io.IOException;
import java.net.SocketException;

/**
 * Provides a connection service using sockets such as reading and writing data.
 */
public final class ConnectionService implements IConnectionPipe {
    private final static int DEFAULT_SLEEPTIME = 25; // 25ms

    private Packer packer = null;
    private String publicKey = null;
    private Connection connection = null;
    private IConnectionService service = null;

    /**
     * Initializes this class.
     * @param sleepTime Amount of time the asynchronous thread sleeps in between reads from the socket.
     */
    public ConnectionService(final int sleepTime, final String publicKey) {
        connection = new Connection(this);
        packer = new Packer(null);

        if (publicKey == null || publicKey.length() == 0)
            throw new IllegalArgumentException("Publickey cannot be empty.");

        this.publicKey = publicKey;

        if (connection.getSleepTime() != sleepTime) {
            connection.setSleepTime(sleepTime);
        }
    }

    /**
     * Initializes this class.
     * @param sleepTime Amount of time the asynchronous thread sleeps in between reads from the socket.
     * @param targetService An instance that implements IConnectionService. This instance will be used as callback
     *                      while reading asynchronous.
     * @throws IOException
     */
    public ConnectionService(final int sleepTime, final String publicKey, final IConnectionService targetService) throws IOException {
        this(sleepTime, publicKey);

        if (targetService == null) {
            throw new IllegalArgumentException("You must implement 'IServiceConnection' to your class and initialize this class with the 'this' keyword in order to use the Async read method.");
        }

        service = targetService;
    }

    /**
     * Initializes this class.
     * @param publicKey An instance that implements IConnectionService. This instance will be used as callback
     *                      while reading asynchronous.
     * @throws IOException
     */
    public ConnectionService(final String publicKey) {
        this(DEFAULT_SLEEPTIME, publicKey);
    }


    /**
     * Initializes this class.
     * @param targetService An instance that implements IConnectionService. This instance will be used as callback
     *                      while reading asynchronous.
     * @throws IOException
     */
    public ConnectionService(final String publicKey, final IConnectionService targetService) throws IOException {
        this(DEFAULT_SLEEPTIME, publicKey, targetService);
    }

    /**
     * Reads data from the input stream.
     * @return A byte array containing the received data from the input stream, or null if no data was read.
     * @throws SocketException If there is no valid connection.
     */
    public ParsedMessage read() throws SocketException {
        if (!connection.isConnected()) {
            throw new SocketException("Socket has no valid or connection, or the valid connection was closed.");
        }

        byte[] buffer = connection.read();
        return packer.unpack(buffer);
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
     * @param classDescriptor The class the data needs to be converted from.
     * @param <T> Protocol buffer class.
     * @return A protocol buffer (T) instance.
     * @throws SocketException An exception occurred while reading data from the stream.
     */
    public <T extends GeneratedMessage> T readGeneric(final Class<T> classDescriptor) throws SocketException, InvalidProtocolBufferException {
        ParsedMessage parsedMessage = this.read();
        if (parsedMessage.getDataMessage().getClass() == classDescriptor)
        {
            return (T)parsedMessage.getDataMessage().getParserForType().parseFrom(parsedMessage.getData());
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

        byte[] buffer = packer.pack(instance, publicKey);
        connection.write(buffer);
    }

    /**
     * Opens a connection to a hostname and port combination.
     * @param hostName Internet protocol address.
     * @param portNumber Port number to connect to.
     * @throws IOException If we couldn't connect to the hostname.
     */
    public void open(final String hostName, final int portNumber) throws IOException {
        connection.open(hostName, portNumber);
    }

    /**
     * Closes the existing connection.
     * @throws IOException
     */
    public void close() throws IOException {
        connection.close();
    }

    /**
     * Checks whether the connection is alive or not.
     * @return True if connected, False if disconnected.
     */
    public boolean isConnected() {
        return connection.isConnected();
    }

    @Override
    public void onReceiveRead(final byte[] buffer) {
        if (service != null) {
            ParsedMessage message = packer.unpack(buffer);
            service.onReceiveRead(message);
        }
    }
}
