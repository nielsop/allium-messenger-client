package nl.han.asd.project.client.commonclient.connection;

import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * An interface that serves as an interface between the Connection and ConnectionService classes.
 */
interface IConnectionPipe {
    void onReceiveRead(final HanRoutingProtocol.EncryptedWrapper wrapper);
}
