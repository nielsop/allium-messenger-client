package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import javax.inject.Inject;

public class MessageStore implements IMessageStore, IMessageObserver {
    private IPersistence persistence;
    private ContactStore contactStore;

    @Inject
    public MessageStore(IPersistence persistence, ContactStore contactStore) {
        this.persistence = persistence;
        this.contactStore = contactStore;
    }

    @Override
    public void addMessage(HanRoutingProtocol.Message message, String receiverUsername) {
        Contact sender = contactStore.findContact(message.getSender());
        Contact receiver = contactStore.findContact(receiverUsername);

        //TODO: implement persistence!
        //persistance.insert("message", new Message(text, sender, receiver, ...);
    }

    @Override public void messageReceived(String confirmationId) {
        //TODO: implement persistence!
    }

    @Override
    public HanRoutingProtocol.Message findMessageByID(String identifier) {
        //TODO: implement!
        //persistance.select("");
        return null;
    }

}
