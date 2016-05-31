package nl.han.asd.project.client.commonclient;

import com.google.inject.AbstractModule;

import nl.han.asd.project.client.commonclient.connection.ConnectionModule;
import nl.han.asd.project.client.commonclient.database.DatabaseModule;
import nl.han.asd.project.client.commonclient.login.LoginModule;
import nl.han.asd.project.client.commonclient.master.MasterModule;
import nl.han.asd.project.client.commonclient.message.MessageModule;
import nl.han.asd.project.client.commonclient.path.PathModule;
import nl.han.asd.project.client.commonclient.persistence.PersistenceModule;
import nl.han.asd.project.client.commonclient.store.StoreModule;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;

/**
 * Created by Marius on 19-04-16.
 */
public class CommonClientModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ConnectionModule());
        install(new DatabaseModule());
        install(new LoginModule());
        install(new MasterModule());
        install(new MessageModule());
        install(new PathModule());
        install(new PersistenceModule());
        install(new StoreModule());
        install(new EncryptionModule());
    }
}
