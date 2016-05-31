package nl.han.asd.project.client.commonclient.message;

import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public interface IMessageBuilder {
    <T extends GeneratedMessage> HanRoutingProtocol.MessageWrapper buildMessage(T generatedMessage , Contact contactReceiver);
}
