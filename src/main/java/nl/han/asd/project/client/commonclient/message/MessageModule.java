package nl.han.asd.project.client.commonclient.message;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.cryptography.EncryptionService;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.node.NodeConnectionService;
import nl.han.asd.project.client.commonclient.path.IGetMessagePath;
import nl.han.asd.project.client.commonclient.path.PathDeterminationService;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.client.commonclient.store.MessageStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;

/**
 * Created by Marius on 19-04-16.
 */
public class MessageModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IGetMessagePath.class).to(PathDeterminationService.class);
        //bind(IEncryptionService.class).to(EncryptionService.class);
        // TODO: Remove this one? This also happens with the this.install(new EncryptionModule) line in the CommonClientModule.
        bind(IMessageStore.class).to(MessageStore.class);
        bind(ISendMessage.class).to(MessageProcessingService.class);
        bind(IMessageBuilder.class).to(MessageBuilderService.class);
    }
}
