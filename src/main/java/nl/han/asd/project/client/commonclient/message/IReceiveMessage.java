package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.protocol.HanRoutingProtocol;

@FunctionalInterface
public interface IReceiveMessage {
    void processMessage(HanRoutingProtocol.MessageWrapper encryptedMessage);
}
