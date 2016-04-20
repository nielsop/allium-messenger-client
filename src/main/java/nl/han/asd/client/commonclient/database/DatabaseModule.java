package nl.han.asd.client.commonclient.database;

import com.google.inject.AbstractModule;
import com.sun.xml.internal.bind.v2.model.core.ID;

/**
 * Created by Marius on 19-04-16.
 */
public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IDatabase.class).to(Database.class);
    }
}
