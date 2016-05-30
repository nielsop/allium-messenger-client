package nl.han.asd.project.client.commonclient.heartbeat;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.connection.IConnectionService;

/**
 * @author Marius
 * @version 1.0
 * @since 30-05-16
 */
public class HeartbeatModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IConnectionService.class).to(HeartbeatService.class);
    }
}
