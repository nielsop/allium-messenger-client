package nl.han.asd.client.commonclient.connection;

/**
 * Created by Marius on 25-04-16.
 */
public interface IConnectionService {
    public void onReceiveRead(byte[] buffer);
}
