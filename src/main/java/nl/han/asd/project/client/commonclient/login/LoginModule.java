package nl.han.asd.project.client.commonclient.login;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;
import nl.han.asd.project.client.commonclient.message.MessageProcessingService;
import nl.han.asd.project.client.commonclient.node.IConnectionListener;
import nl.han.asd.project.client.commonclient.node.ISetConnectedNodes;
import nl.han.asd.project.client.commonclient.node.NodeConnection;
import nl.han.asd.project.client.commonclient.node.NodeConnectionService;

/**
 * Created by Marius on 19-04-16.
 */
public class LoginModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IAuthentication.class).to(MasterGateway.class);
        bind(ISetConnectedNodes.class).to(NodeConnectionService.class);
        bind(IReceiveMessage.class).to(MessageProcessingService.class);
        bind(IConnectionListener.class).to(NodeConnection.class);
        bind(ILogin.class).to(LoginService.class);
    }
}
