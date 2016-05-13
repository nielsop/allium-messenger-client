package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.connection.UnpackedMessage;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public interface IReceiveMessage {
    public void processMessage(UnpackedMessage unpackedMessage);
}
