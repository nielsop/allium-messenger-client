package nl.han.onionmessenger.commonclient.message;

import nl.han.onionmessenger.commonclient.cryptography.IDecrypt;
import nl.han.onionmessenger.commonclient.cryptography.IEncrypt;
import nl.han.onionmessenger.commonclient.node.ISendMessage;
import nl.han.onionmessenger.commonclient.path.IGetPath;
import nl.han.onionmessenger.commonclient.store.IMessage;

public class MessageBuilderService implements IConfirmationMessageBuilder, IMessageBuilder {
    public IGetPath getPath;
    public ISendMessage sendMessage;
    public IMessage message;
    public IEncrypt encrypt;
    public IDecrypt decrypt;
}
