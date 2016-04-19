package nl.han.asd.client.commonclient.message;

import nl.han.asd.client.commonclient.path.IGetPath;
import nl.han.asd.client.commonclient.store.IMessageStore;
import nl.han.asd.client.commonclient.node.ISendMessage;

import javax.inject.Inject;


public class MessageBuilderService implements IMessageBuilder {
    public IGetPath getPath;
    public ISendMessage sendMessage;
    public IMessageStore messageStore;

    @Inject
    public MessageBuilderService(IGetPath getPath, ISendMessage sendMessage, IMessageStore messageStore) {
        this.getPath = getPath;
        this.sendMessage = sendMessage;
        this.messageStore = messageStore;
    }
}
