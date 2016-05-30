package nl.han.asd.project.client.commonclient.database;

import com.google.inject.AbstractModule;

public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IDatabase.class).to(HyperSQLDatabase.class);
    }
}
