package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Raoul on 31/5/2016.
 */
public interface IMessageConfirmation {
    public void messageSent(String id, Message message, Contact contact);

    public void messageConfirmationReceived(String id);
}
