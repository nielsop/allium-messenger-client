package nl.han.asd.project.client.commonclient.connection;

/**
 * An interface used to return data when asynchronously reading on a socket.
 *
 * @author Jevgeni Geurtsen
 */
public interface IConnectionService {
    void onReceiveRead(final UnpackedMessage message);
}
