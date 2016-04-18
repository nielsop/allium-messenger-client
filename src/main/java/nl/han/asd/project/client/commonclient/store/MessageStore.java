package nl.han.asd.project.client.commonclient.store;


import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.client.commonclient.presentation.IMessageObserver;

public class MessageStore implements IMessage {
    public IPersistence persistence;
    public IMessageObserver messageObserver;
}
