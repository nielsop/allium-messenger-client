package nl.han.asd.client.commonclient.store;

import nl.han.asd.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;

public class MessageStore implements IMessageStore, IMessageObserver {
    public IPersistence persistence;

    @Inject
    public MessageStore(IPersistence persistence) {
        this.persistence = persistence;
    }
}
