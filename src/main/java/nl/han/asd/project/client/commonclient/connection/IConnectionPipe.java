package nl.han.asd.project.client.commonclient.connection;

/**
 * Created by BILLPOORTS on 25-4-2016.
 */
interface IConnectionPipe {
    public void onReceiveRead(byte[] buffer);
}
