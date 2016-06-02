package nl.han.asd.project.client.commonclient.message;

public interface IMessageReceiver {
    /**
     * Forward a received message
     *
     * @param message The internal message object that is received
     */
    public void receivedMessage(Message message);
}
