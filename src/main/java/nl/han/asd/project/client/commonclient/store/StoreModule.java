package nl.han.asd.project.client.commonclient.store;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.database.HyperSQLDatabase;
import nl.han.asd.project.client.commonclient.database.IDatabase;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.client.commonclient.persistence.PersistenceService;

public class StoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IDatabase.class).to(HyperSQLDatabase.class);
        bind(IPersistence.class).to(PersistenceService.class);
        bind(IContactStore.class).to(ContactStore.class);
        bind(IMessageStore.class).to(MessageStore.class);
    }
}
