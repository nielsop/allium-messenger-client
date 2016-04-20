package nl.han.asd.project.client.commonclient.database;

import com.google.inject.AbstractModule;

/**
 * Created by Marius on 19-04-16.
 */
public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IDatabase.class).to(Database.class);
    }
}
