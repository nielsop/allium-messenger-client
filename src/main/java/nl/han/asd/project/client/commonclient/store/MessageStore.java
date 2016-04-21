package nl.han.asd.project.client.commonclient.store;


import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;

public class MessageStore implements IMessageStore, IMessageObserver {
    public IPersistence persistence;

    @Inject
    public MessageStore(IPersistence persistence) {
        this.persistence = persistence;
    }
}
