package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.client.commonclient.presentation.IMessageObserver;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public class MessageStore implements IMessage {
    public IPersistence persistence;
    public IMessageObserver messageObserver;

    @Override
    public void addMessageToStore(HanRoutingProtocol.Message message) {

    }

    @Override
    public void addFileChunkToStore(HanRoutingProtocol.Message message) {

    }
}
