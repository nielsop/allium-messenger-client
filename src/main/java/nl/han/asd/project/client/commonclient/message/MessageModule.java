package nl.han.asd.project.client.commonclient.message;

import com.google.inject.AbstractModule;

public class MessageModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IReceiveMessage.class).to(MessageProcessingService.class);
    }
}
