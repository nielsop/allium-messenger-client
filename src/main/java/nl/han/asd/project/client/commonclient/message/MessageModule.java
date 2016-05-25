package nl.han.asd.project.client.commonclient.message;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.node.NodeGateway;
import nl.han.asd.project.client.commonclient.path.IGetPath;
import nl.han.asd.project.client.commonclient.path.PathDeterminationService;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.client.commonclient.store.MessageStore;

/**
 * Created by Marius on 19-04-16.
 */
public class MessageModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IGetPath.class).to(PathDeterminationService.class);
        bind(IMessageStore.class).to(MessageStore.class);
        bind(ISendMessage.class).to(NodeGateway.class);
        bind(IMessageBuilder.class).to(MessageBuilderService.class);
    }
}
