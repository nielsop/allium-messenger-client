package nl.han.asd.project.client.commonclient.master;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.heartbeat.IHeartbeat;

/**
 * Created by Marius on 19-04-16.
 */
public class MasterModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IAuthentication.class).to(MasterGateway.class);
        bind(IGetGraphUpdates.class).to(MasterGateway.class);
        bind(IGetClientGroup.class).to(MasterGateway.class);
        bind(IRegistration.class).to(MasterGateway.class);
        bind(IHeartbeat.class).to(MasterGateway.class);
    }
}
