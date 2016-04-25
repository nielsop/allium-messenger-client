package nl.han.asd.client.commonclient.master;

import com.google.inject.AbstractModule;

/**
 * Created by Marius on 19-04-16.
 */
public class MasterModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IHeartbeat.class).to(MasterGateway.class);
        bind(IGetClientGroup.class).to(MasterGateway.class);
        bind(IGetUpdatedGraph.class).to(MasterGateway.class);
        bind(IAuthentication.class).to(MasterGateway.class);
        bind(IRegistration.class).to(MasterGateway.class);
    }
}
