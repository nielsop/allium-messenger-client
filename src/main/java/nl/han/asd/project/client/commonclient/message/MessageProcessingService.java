package nl.han.asd.project.client.commonclient.message;

import com.amazonaws.services.cloudfront.model.InvalidArgumentException;
import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.store.IMessage;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public class MessageProcessingService implements IReceiveMessage {
    public IConfirmationMessageBuilder confirmationMessageBuilder; // MessageBuilderService?
    public IDecrypt decrypt;
    public IMessage message;

    public MessageProcessingService(IConfirmationMessageBuilder confirmationMessageBuilder) {
        this.confirmationMessageBuilder = confirmationMessageBuilder;
    }

    @Override
    public void receiveMessage(EncryptedMessage message) {
        if (message == null)
            throw new InvalidArgumentException("message");

        handleProcessing(message);
    }

    private void handleProcessing(EncryptedMessage encryptedMessage) {
        HanRoutingProtocol.Message message = decrypt.decryptEncryptedMessage(encryptedMessage);

        //if (message.getType() == Message)
        //{
            this.message.addMessageToStore(message);
        //}
        //else
        //{
        //    this.message.addFileChunkToStore(id?, message);
        //}
        confirmationMessageBuilder.sendConfirmationMessage(message.getId(), message.getSender() /* CONTACT? */);
    }

}
