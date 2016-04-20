package nl.han.asd.client.commonclient.message;

import com.google.inject.AbstractModule;

/**
 * Created by Marius on 19-04-16.
 */
public class MessageModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IMessageBuilder.class).to(MessageBuilderService.class);
        bind(IReceiveMessage.class).to(MessageProcessingService.class);
    }
}