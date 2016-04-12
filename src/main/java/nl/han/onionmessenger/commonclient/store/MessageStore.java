package nl.han.onionmessenger.commonclient.store;

import nl.han.onionmessenger.commonclient.persistence.IPersistence;
import nl.han.onionmessenger.commonclient.presentation.IMessageObserver;

public class MessageStore implements IMessage {
    public IPersistence persistence;
    public IMessageObserver messageObserver;
}
