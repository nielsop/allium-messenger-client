package nl.han.asd.project.client.commonclient.node;

import com.google.inject.AbstractModule;

/**
 * Created by Marius on 19-04-16.
 */
public class NodeModule extends AbstractModule {
    @Override
    protected void configure() {
        //IReceiveMessage receiveMessage, INodeConnection nodeConnection
//        bind(INodeGateway.class).to(NodeGateway.class);
        //  bind(INodeConnection.class).to(NodeConnectionService.class);
        //TODO: fix dependency injection
    }
}
