package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Bind the connection implementations to the
 * interfaces.
 *
 * @version 1.0
 */
public class ConnectionModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(IConnectionService.class, ConnectionService.class)
                .build(IConnectionServiceFactory.class));
    }

}
