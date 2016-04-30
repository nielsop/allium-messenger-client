package nl.han.asd.project.client.commonclient.connection;

/**
 *
 * @author Jevgeni Geurtsen
 */
public interface IConnectionService {
    public void onReceiveRead(byte[] buffer);
}
