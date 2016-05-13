package nl.han.asd.project.client.commonclient.persistence;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.database.Database;
import nl.han.asd.project.client.commonclient.database.IDatabase;

/**
 * Created by Marius on 19-04-16.
 */
public class PersistenceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IPersistence.class).to(PersistenceService.class);
        bind(IDatabase.class).to(Database.class);
    }
}
