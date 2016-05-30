package nl.han.asd.project.client.commonclient.node;

import com.google.inject.AbstractModule;

public class NodeModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ISendData.class).to(NodeConnectionService.class);
        bind(ISetConnectedNodes.class).to(NodeConnectionService.class);
        bind(IConnectionListener.class).to(NodeConnection.class);
    }
}
