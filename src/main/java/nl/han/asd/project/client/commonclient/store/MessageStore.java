package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.presentation.IMessageObserver;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;


public class MessageStore implements IMessage {
    public IPersistence persistence;
    public IMessageObserver messageObserver;
}
