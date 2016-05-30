package nl.han.asd.project.client.commonclient.heartbeat;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.connection.IConnectionService;

public class HeartbeatModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IConnectionService.class).to(HeartbeatService.class);
    }
}