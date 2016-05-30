package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.AbstractModule;

public class ConnectionModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IConnectionPipe.class).to(ConnectionService.class);
    }
}
