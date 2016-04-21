package nl.han.asd.project.client.commonclient.message;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.node.NodeGateway;

/**
 * Created by Marius on 19-04-16.
 */
public class MessageModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IMessageBuilder.class).to(MessageBuilderService.class);
        bind(IReceiveMessage.class).to(MessageProcessingService.class);
        bind(IDecrypt.class).to(CryptographyService.class);
        bind(ISendMessage.class).to(NodeGateway.class);
    }
}
