package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;

public interface ISendMessage {
    /**
     * Send a message to a user.
     *
     * @param message Message to be sent.
     * @param contact User to send the message to.
     */
    public void sendMessage(Message message, Contact contact);
}
