package nl.han.asd.project.client.commonclient;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.database.DatabaseModule;
import nl.han.asd.project.client.commonclient.login.LoginModule;
import nl.han.asd.project.client.commonclient.master.MasterModule;
import nl.han.asd.project.client.commonclient.message.MessageModule;
import nl.han.asd.project.client.commonclient.node.NodeModule;
import nl.han.asd.project.client.commonclient.path.PathModule;
import nl.han.asd.project.client.commonclient.persistence.PersistenceModule;
import nl.han.asd.project.client.commonclient.store.StoreModule;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.EncryptionService;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;

/**
 * Created by Marius on 19-04-16.
 */
public class CommonclientModule extends AbstractModule {
    @Override
    protected void configure() {
        this.install(new DatabaseModule());
        this.install(new LoginModule());
        this.install(new MasterModule());
        this.install(new MessageModule());
        this.install(new NodeModule());
        this.install(new PathModule());
        this.install(new PersistenceModule());
        this.install(new StoreModule());
        this.install(new EncryptionModule());
        this.bind(IEncryptionService.class).to(EncryptionService.class);
    }
}
