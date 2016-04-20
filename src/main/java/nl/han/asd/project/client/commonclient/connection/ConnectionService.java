package nl.han.asd.project.client.commonclient.connection;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;

/**
 * Provides a connection service using sockets such as reading and writing.
 */
public final class ConnectionService implements Observer {
    private final static int DEFAULT_SLEEPTIME = 25; // 25ms

    private Connection connection = null;
    private IConnectionService service = null;

    public ConnectionService(int sleepTime) {
        connection = new Connection();

        if (connection.getSleepTime() != sleepTime) {
            connection.setSleepTime(sleepTime);
        }
    }

    public ConnectionService(int sleepTime, IConnectionService targetService) throws IOException {
        this(sleepTime);

        if (targetService == null)
            throw new RuntimeException("You must implement 'IServiceConnection' to your class and initialize this class with the 'this' keyword in order to use the Async read method.");

        service = targetService;
    }

    public ConnectionService(){
        this(DEFAULT_SLEEPTIME);
    }

    public ConnectionService(IConnectionService targetService) throws IOException {
        this(DEFAULT_SLEEPTIME, targetService);
    }

    public void readAsync() throws SocketException {
        if (!connection.isConnected())
            throw new SocketException("Socket has no valid or connection, or the valid connection was closed.");
        if (service == null)
            throw new RuntimeException("You must implement 'IServiceConnection' to your class and initialize this class with the 'this' keyword in order to use the Async read method.");

        // uses observer
        connection.readAsync();
    }

    public void stopReadAsync() throws SocketException {
        if (!connection.isConnected())
            throw new SocketException("Socket has no valid or connection, or the valid connection was closed.");

        connection.stopReadAsync();
    }

    public byte[] read() throws SocketException {
        if (!connection.isConnected())
            throw new SocketException("Socket has no valid or connection, or the valid connection was closed.");

        return connection.read();
    }

    public <T extends GeneratedMessage> T readGeneric(Class<T> classDescriptor) throws SocketException {
        byte[] buffer = this.read();
        try {
            Field defaultInstanceField = classDescriptor.getDeclaredField("DEFAULT_INSTANCE");
            defaultInstanceField.setAccessible(true);
            T defaultInstance = (T)defaultInstanceField.get(null);
            return (T)defaultInstance.getParserForType().parseFrom(buffer);
        } catch (IllegalAccessException | InvalidProtocolBufferException e) {
            // return null
        } catch (NoSuchFieldException e) {
            new InvalidProtocolBufferException("Invalid class provided.");
        }
        return null;
    }

    public void write(byte[] data) throws SocketException {
        if (!connection.isConnected())
            throw new SocketException("Socket has no valid or connection, or the valid connection was closed.");

        connection.write(data);
    }

    public <T extends GeneratedMessage.Builder> void writeGeneric(T instance) throws SocketException {
        this.write(instance.build().toByteArray());
    }

    public void open(String hostName, int portNumber) throws IOException {
        connection.open(hostName, portNumber);
        connection.addObserver(this);
    }

    public void close() throws IOException {
        connection.close();
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (service == null)
            throw new RuntimeException("You must implement 'IServiceConnection' to your class and initialize this class with the 'this' keyword in order to use the Async read method.");

        service.onReceiveRead((byte[]) arg);
    }
}
