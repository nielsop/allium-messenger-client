package nl.han.asd.project.client.commonclient.message;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.path.IGetMessagePath;
import nl.han.asd.project.client.commonclient.path.PathDeterminationService;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.client.commonclient.store.MessageStore;

public class MessageModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IMessageBuilder.class).to(MessageBuilderService.class);
        bind(IReceiveMessage.class).to(MessageProcessingService.class);
        bind(ISendMessage.class).to(MessageProcessingService.class);
    }
}
