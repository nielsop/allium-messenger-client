package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.path.IGetPath;
import nl.han.asd.project.client.commonclient.store.IMessage;

public class MessageBuilderService implements IConfirmationMessageBuilder, IMessageBuilder {
    public IGetPath getPath;
    public ISendMessage sendMessage;
    public IMessage message;
    public IEncrypt encrypt;
    public IDecrypt decrypt;
}
