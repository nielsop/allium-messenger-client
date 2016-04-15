package unit.commonclient.message;

import unit.commonclient.cryptography.IDecrypt;
import unit.commonclient.node.ISendMessage;
import unit.commonclient.path.IGetPath;
import unit.commonclient.store.IMessage;
import unit.commonclient.cryptography.IEncrypt;


public class MessageBuilderService implements IConfirmationMessageBuilder, IMessageBuilder {
    public IGetPath getPath;
    public ISendMessage sendMessage;
    public IMessage message;
    public IEncrypt encrypt;
    public IDecrypt decrypt;
}
