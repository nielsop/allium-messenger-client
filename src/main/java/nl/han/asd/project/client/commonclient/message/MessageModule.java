package nl.han.asd.project.client.commonclient.message;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.path.IGetMessagePath;
import nl.han.asd.project.client.commonclient.path.PathDeterminationService;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.client.commonclient.store.MessageStore;

public class MessageModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IGetMessagePath.class).to(PathDeterminationService.class);
        bind(IMessageStore.class).to(MessageStore.class);
        bind(ISendMessage.class).to(MessageProcessingService.class);
        bind(IReceiveMessage.class).to(MessageProcessingService.class);
    }
}
