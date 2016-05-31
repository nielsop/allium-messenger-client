package nl.han.asd.project.client.commonclient.message;

import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.client.commonclient.store.Contact;

@FunctionalInterface
public interface ISendMessage {
    <T extends GeneratedMessage> void sendMessage(T message, Contact contactReciever);
}
