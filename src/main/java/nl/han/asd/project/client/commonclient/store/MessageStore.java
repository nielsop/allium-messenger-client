package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import javax.inject.Inject;

public class MessageStore implements IMessageStore, IMessageObserver {
    private IPersistence persistence;

    @Inject
    public MessageStore(IPersistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public void addMessage(HanRoutingProtocol.Message message) {
        //TODO: implement!
    }

    @Override
    public void findMessage(HanRoutingProtocol.Message message) {
        //TODO: implement!
    }
}
