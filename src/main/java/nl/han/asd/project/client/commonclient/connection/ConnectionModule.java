package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Created by Jevgeni on 18-5-2016.
 */
public class ConnectionModule extends AbstractModule {
    @Override protected void configure() {
        install(new FactoryModuleBuilder().build(IConnectionServiceFactory.class));
    }
}
