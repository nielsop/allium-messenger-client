package nl.han.asd.project.client.commonclient.message;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.node.ISendMessage;

public class MessageModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IMessageBuilder.class).to(MessageBuilderService.class);
        bind(IReceiveMessage.class).to(MessageProcessingService.class);
        bind(ISendMessage.class).to(MessageProcessingService.class);
    }
}
