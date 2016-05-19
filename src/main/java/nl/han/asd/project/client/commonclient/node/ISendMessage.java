package nl.han.asd.project.client.commonclient.node;

import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.client.commonclient.store.Contact;

public interface ISendMessage {
    <T extends GeneratedMessage> void sendMessage(T message, Contact contactReciever, Contact contactSender);
}
