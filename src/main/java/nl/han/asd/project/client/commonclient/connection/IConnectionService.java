package nl.han.asd.project.client.commonclient.connection;

/**
 * @author Jevgeni Geurtsen
 *         An interface used to return data when asynchronously reading on a socket.
 */
public interface IConnectionService {
    void onReceiveRead(final UnpackedMessage message);
}
