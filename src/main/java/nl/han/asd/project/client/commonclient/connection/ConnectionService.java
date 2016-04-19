package nl.han.asd.project.client.commonclient.connection;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jevgeni on 18-4-2016.
 */
public class ConnectionService implements Observer {
    private Connection connection = null;
    private IConnectionService service = null;

    public ConnectionService() { }

    public ConnectionService(IConnectionService targetService) throws IOException {
        service = targetService;
    }

    public void ReadAsync() {
        if (service == null)
            throw new RuntimeException("No interface implemented");

        // uses observer
        connection.Read(this);
    }

    public byte[] Read()
    {
        return connection.Read();
    }

    public void Write(byte[] data) {
        try {
            connection.Write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Start(String hostName, int portNumber) throws IOException {
        connection = new Connection(hostName, portNumber);
    }

    public void Close() throws IOException {
        connection.Close();
    }

    @Override
    public void update(Observable o, Object arg) {
        service.OnReceiveRead((byte[]) arg);
    }
}
