package nl.han.asd.client.commonclient.store;

import nl.han.asd.client.commonclient.persistence.IPersistence;
import nl.han.asd.client.commonclient.presentation.IMessageObserver;


public class MessageStore implements IMessage {
    public IPersistence persistence;
    public IMessageObserver messageObserver;
}
