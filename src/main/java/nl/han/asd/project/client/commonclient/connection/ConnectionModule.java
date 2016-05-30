package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.AbstractModule;

/**
 * @author Marius
 * @version 1.0
 * @since 30-05-16
 */
public class ConnectionModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(IConnectionPipe.class).to(ConnectionService.class);
    }
}
