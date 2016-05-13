package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.protocol.HanRoutingProtocol;

public interface IMessageStore {
    void addMessage(HanRoutingProtocol.Message message, String receiverUsername);

    HanRoutingProtocol.Message findMessageByID(String identifier);
}
