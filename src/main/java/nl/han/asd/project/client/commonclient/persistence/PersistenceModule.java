package nl.han.asd.project.client.commonclient.persistence;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.database.HyperSQLDatabase;
import nl.han.asd.project.client.commonclient.database.IDatabase;

public class PersistenceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IPersistence.class).to(PersistenceService.class);
        bind(IDatabase.class).to(HyperSQLDatabase.class);
    }
}
