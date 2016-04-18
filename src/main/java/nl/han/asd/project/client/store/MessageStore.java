package nl.han.asd.project.client.store;

import nl.han.asd.project.client.presentation.IMessageObserver;
import nl.han.asd.project.client.persistence.IPersistence;


public class MessageStore implements IMessage {
    public IPersistence persistence;
    public IMessageObserver messageObserver;
}
