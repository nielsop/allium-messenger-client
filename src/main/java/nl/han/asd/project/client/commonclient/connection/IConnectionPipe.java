package nl.han.asd.project.client.commonclient.connection;

/**
 * An interface that serves as an interface between the Connection and ConnectionService classes.
 */
interface IConnectionPipe {
    void onReceiveRead(final byte[] buffer);
}
