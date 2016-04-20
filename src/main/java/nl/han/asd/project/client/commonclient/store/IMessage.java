package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.protocol.HanRoutingProtocol;

public interface IMessage {
    public void addMessageToStore(HanRoutingProtocol.Message message);
    public void addFileChunkToStore(HanRoutingProtocol.Message message);
}
