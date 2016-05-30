package nl.han.asd.project.client.commonclient.database;

import com.google.inject.AbstractModule;

/**
 *
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 24-5-2016
 */
public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(String.class).toInstance("string");
        bind(IDatabase.class).to(HyperSQLDatabase.class);

    }
}
