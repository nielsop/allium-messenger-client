package unit.commonclient.store;

import unit.commonclient.presentation.IMessageObserver;
import unit.commonclient.persistence.IPersistence;


public class MessageStore implements IMessage {
    public IPersistence persistence;
    public IMessageObserver messageObserver;
}
