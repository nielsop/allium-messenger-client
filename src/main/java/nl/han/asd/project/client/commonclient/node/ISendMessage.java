package nl.han.asd.project.client.commonclient.node;

import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.client.commonclient.message.EncryptedMessage;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.protocol.HanRoutingProtocol;

@FunctionalInterface
public interface ISendMessage {
    void sendMessage(HanRoutingProtocol.MessageWrapper messageWrapper, Contact contactReceiver);
}
