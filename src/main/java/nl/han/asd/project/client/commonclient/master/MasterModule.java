package nl.han.asd.project.client.commonclient.master;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.heartbeat.IHeartbeat;

public class MasterModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IGetUpdatedGraph.class).to(MasterGateway.class);
        bind(IGetClientGroup.class).to(MasterGateway.class);
        bind(IRegistration.class).to(MasterGateway.class);
        bind(IHeartbeat.class).to(MasterGateway.class);
        bind(IAuthentication.class).to(MasterGateway.class);
        bind(ILogout.class).to(MasterGateway.class);
    }
}
