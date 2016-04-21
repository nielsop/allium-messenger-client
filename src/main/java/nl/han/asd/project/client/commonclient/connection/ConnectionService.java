package nl.han.asd.project.client.commonclient.connection;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.SocketException;

/**
 * Provides a connection service using sockets such as reading and writing data.
 */
public final class ConnectionService {
    private final static int DEFAULT_SLEEPTIME = 25; // 25ms

    private Connection connection = null;
    private IConnectionService service = null;

    /**
     * Initializes this class.
     * @param sleepTime Amount of time the asynchronous thread sleeps in between reads from the socket.
     */
    public ConnectionService(int sleepTime) {
        connection = new Connection();

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
    public ConnectionService(int sleepTime, IConnectionService targetService) throws IOException {
        this(sleepTime);

        if (targetService == null) {
            throw new IllegalArgumentException("You must implement 'IServiceConnection' to your class and initialize this class with the 'this' keyword in order to use the Async read method.");
        }

        service = targetService;
    }

    /**
     * Initializes this class.
     */
    public ConnectionService(){
        this(DEFAULT_SLEEPTIME);
    }

    /**
     * Initializes this class.
     * @param targetService An instance that implements IConnectionService. This instance will be used as callback
     *                      while reading asynchronous.
     * @throws IOException
     */
    public ConnectionService(IConnectionService targetService) throws IOException {
        this(DEFAULT_SLEEPTIME, targetService);
    }

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
     *
     * @return A byte array containing the received data from the input stream, or null if no data was read.
     * @throws SocketException If there is no valid connection.
     */
    public byte[] read() throws SocketException {
        if (!connection.isConnected()) {
            throw new SocketException("Socket has no valid or connection, or the valid connection was closed.");
        }

        return connection.read();
    }

    /**
     * Synchronously (blocking) read a the input stream. Then converts the results into an instance of @classDescriptor.
     * @param classDescriptor The class the data needs to be converted from.
     * @param <T> Protocol buffer class.
     * @return A protocol buffer (T) instance.
     * @throws SocketException An exception occurred while reading data from the stream.
     */
    public <T extends GeneratedMessage> T readGeneric(Class<T> classDescriptor) throws SocketException, InvalidProtocolBufferException {
        byte[] buffer = this.read();
        try {
            Field defaultInstanceField = classDescriptor.getDeclaredField("DEFAULT_INSTANCE");
            defaultInstanceField.setAccessible(true);
            T defaultInstance = (T)defaultInstanceField.get(null);
            return (T)defaultInstance.getParserForType().parseFrom(buffer);
        } catch (IllegalAccessException | InvalidProtocolBufferException e) {
            // return null
        } catch (NoSuchFieldException e) {
            throw new InvalidProtocolBufferException("Invalid class provided.");
        }
        return null;
    }

    /**
     * Writes data to the connection using the output stream.
     * @param data Data to write.
     * @throws SocketException An exception occurred while writing the data.
     */
    public void write(byte[] data) throws SocketException {
        if (!connection.isConnected()) {
            throw new SocketException("Socket has no valid or connection, or the valid connection was closed.");
        }

        connection.write(data);
    }

    /**
     * Writes data from the builder to the connection using the input stream.
     * @param instance Instance of the builder class of the protocol buffer.
     * @param <T> Type that extends GeneratedMessage.Builder.
     * @throws SocketException An exception occurred while writing the data.
     */
    public <T extends GeneratedMessage.Builder> void writeGeneric(T instance) throws SocketException {
        this.write(instance.build().toByteArray());
    }

    /**
     * Opens a connection to a hostname and port combination.
     * @param hostName Internet protocol address.
     * @param portNumber Port number to connect to.
     * @throws IOException If we couldn't connect to the hostname.
     */
    public void open(String hostName, int portNumber) throws IOException {
        connection.open(hostName, portNumber);
        connection.setConnectionService(service);
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

}
