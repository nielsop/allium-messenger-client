package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.protocol.HanRoutingProtocol;

public interface IMessageStore {
    public void addMessage(HanRoutingProtocol.Message message);
    public void findMessage(HanRoutingProtocol.Message message);
}
