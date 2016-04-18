package nl.han.asd.project.client.message;

import nl.han.asd.project.client.cryptography.IDecrypt;
import nl.han.asd.project.client.node.ISendMessage;
import nl.han.asd.project.client.path.IGetPath;
import nl.han.asd.project.client.store.IMessage;
import nl.han.asd.project.client.cryptography.IEncrypt;


public class MessageBuilderService implements IConfirmationMessageBuilder, IMessageBuilder {
    public IGetPath getPath;
    public ISendMessage sendMessage;
    public IMessage message;
    public IEncrypt encrypt;
    public IDecrypt decrypt;
}
