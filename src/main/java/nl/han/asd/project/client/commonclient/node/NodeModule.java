package nl.han.asd.project.client.commonclient.node;

import com.google.inject.AbstractModule;

/**
 * Created by Marius on 19-04-16.
 */
public class NodeModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ISendMessage.class).to(NodeGateway.class);
        bind(ISetConnectedNodes.class).to(NodeConnectionService.class);
    }
}
