package nl.han.asd.client.commonclient;

import com.google.inject.AbstractModule;
import nl.han.asd.client.commonclient.database.DatabaseModule;
import nl.han.asd.client.commonclient.login.LoginModule;
import nl.han.asd.client.commonclient.master.MasterModule;
import nl.han.asd.client.commonclient.message.MessageModule;
import nl.han.asd.client.commonclient.node.NodeModule;
import nl.han.asd.client.commonclient.path.PathModule;
import nl.han.asd.client.commonclient.persistence.PersistenceModule;
import nl.han.asd.client.commonclient.store.StoreModule;

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
    }
}
