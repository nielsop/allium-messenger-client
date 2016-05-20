package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MessageStore {
    public IPersistence persistence;

    @Inject
    public MessageStore(IPersistence persistence) {
        this.persistence = persistence;
    }

    static public void addMessage(HanRoutingProtocol.Message message) {
        //TODO: implement!
    }

    static public void findMessage(HanRoutingProtocol.Message message) {
        //TODO: implement!
    }

    static public List<Message> getMessagesAfterDate(String date) {
        return new ArrayList<>();
    }
}
