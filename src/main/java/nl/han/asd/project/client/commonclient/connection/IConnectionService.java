package nl.han.asd.project.client.commonclient.connection;

/**
 * Created by Marius on 25-04-16.
 * An interface used to return data when asynchronously reading on a socket.
 */
public interface IConnectionService {
    void onReceiveRead(final UnpackedMessage message);
}
