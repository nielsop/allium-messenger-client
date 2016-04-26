package nl.han.asd.project.client.commonclient.connection;

/**
 * Created by Jevgeni on 25-4-2016.
 */
interface IConnectionPipe {
    public void onReceiveRead(final byte[] buffer);
}
