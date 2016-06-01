package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * @author Julius
 * @version 1.0
 * @since 24/05/16
 */
@FunctionalInterface
public interface ISendData {
    void sendData(HanRoutingProtocol.MessageWrapper messageWrapper) throws MessageNotSentException;
}
