package nl.han.asd.project.client.commonclient.login;

import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.node.ISetConnectedNodes;
import nl.han.asd.project.client.commonclient.utility.Validation;

import javax.inject.Inject;

/**
 * Created by Marius on 19-04-16.
 */
public class LoginService implements ILogin {

    private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]";
    private static final String REGEX_ALPHANUMERICSPECIAL = "^(?=(?:\\D*?\\d){8,32}(?!.*?\\d))[a-zA-Z0-9@\\#$%&*()_+\\]\\[';:?.,!^-]+$";
    private MasterGateway masterGateway = null;
    private ISetConnectedNodes setConnectedNodes;
    private IAuthentication authentication;

    @Inject
    public LoginService(ISetConnectedNodes setConnectedNodes, IAuthentication authentication, MasterGateway gateway) {
        this.setConnectedNodes = setConnectedNodes;
        this.authentication = authentication;
        this.masterGateway = gateway;
        //Injector injector = Guice.createInjector(new EncryptionModule());
        //this.masterGateway = new MasterGateway(injector.getInstance(IEncryptionService.class));
        //this.masterGateway.setConnectionData(Configuration.HOSTNAME, Configur);
    }

    public LoginResponseWrapper login(String username, String password) {
        // DO NOT REMOVE, YET!
        if (Validation.validateCredentials(username, password))
            return masterGateway.authenticate(username, password);
        else
            return null;
    }
}
