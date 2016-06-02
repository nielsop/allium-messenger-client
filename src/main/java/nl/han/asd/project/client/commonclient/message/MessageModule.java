package nl.han.asd.project.client.commonclient.message;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.path.IGetMessagePath;
import nl.han.asd.project.client.commonclient.path.PathDeterminationService;

public class MessageModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IGetMessagePath.class).to(PathDeterminationService.class);
        bind(ISendMessage.class).to(MessageProcessingService.class);
        bind(IReceiveMessage.class).to(MessageProcessingService.class);
        bind(IMessageConfirmation.class).to(MessageConfirmationService.class);
        bind(IMessageBuilder.class).to(MessageBuilderService.class);
        bind(ISubscribeMessageReceiver.class).to(MessageProcessingService.class);
    }
}
