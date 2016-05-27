package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.Configuration;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.master.wrapper.ClientGroupResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.UpdatedGraphResponseWrapper;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class MasterGateway implements IGetGraphUpdates, IGetClientGroup, IRegistration, IHeartbeat, IAuthentication {
    //TODO: missing: IWebService from Master

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterGateway.class);
    private ConnectionService connectionService;
    private Socket socket;
    private IEncryptionService encryptionService;
    private String hostname = Configuration.getHostname();
    private int port = Configuration.getPort();

    /**
     * Constructs the MasterGateway.
     * Gets an instance of the encryption service using Guice dependency injection.
     * @param encryptionService
     */
    @Inject
    public MasterGateway(IEncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public void setConnectionData(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Gets the socket if there already was a connection.
     * It creates a new socket if there is no socket or the previous has been closed.
     * @return Socket socket.
     */
    public Socket getSocket() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket(hostname, port);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return socket;
    }

    /**
     * This method authenticates a user to the master application with the given credentials.
     * @param username the username to be authenticated.
     * @param password the password belonging to the username.
     * @return a wrapper which is the response of the authentication process. For the specific details of the LoginResponseWrapper,
     *          check the LoginResponseWrapper class.
     */
    @Override
    public LoginResponseWrapper authenticate(String username, String password) throws IllegalArgumentException {
        Validation.validateCredentials(username, password);
        HanRoutingProtocol.ClientLoginRequest loginRequest = HanRoutingProtocol.ClientLoginRequest.newBuilder().setUsername(username).setPassword(password)
                .setPublicKey(ByteString.copyFrom(encryptionService.getPublicKey())).build();
        RequestWrapper request = new RequestWrapper(loginRequest, HanRoutingProtocol.Wrapper.Type.CLIENTLOGINREQUEST, getSocket());
        HanRoutingProtocol.ClientLoginResponse response = request.writeAndRead(HanRoutingProtocol.ClientLoginResponse.class);
        //TODO: Response kan null zijn als er op de master niet ingelogd kan worden (wordt nu afgevangen op master). Controle voor response=null toevoegen?
        return new LoginResponseWrapper(response.getConnectedNodesList(), response.getSecretHash(), response.getStatus());
    }

    /**
     * This method registers a user at the master application with the given credentials.
     * @param username the username to be registered.
     * @param password the password to be registered with the username.
     * @param passwordRepeat repeat the password to guarantee the user he inserted the password he really wanted to use.
     * @return a wrapper which is the response of the registration process. For the specific details of the RegisterResponseWrapper,,
     *          check the RegisterResponseWrapper class.
     * @throws IllegalArgumentException
     */
    @Override
    public RegisterResponseWrapper register(String username, String password, String passwordRepeat) throws IllegalArgumentException {
        Validation.validateCredentials(username, password);
        Validation.passwordsEqual(password, passwordRepeat);
        HanRoutingProtocol.ClientRegisterRequest registerRequest = HanRoutingProtocol.ClientRegisterRequest.newBuilder().setUsername(username).setPassword(password).build();
        RequestWrapper req = new RequestWrapper(registerRequest, HanRoutingProtocol.Wrapper.Type.CLIENTREGISTERREQUEST, getSocket());
        HanRoutingProtocol.ClientRegisterResponse response = req.writeAndRead(HanRoutingProtocol.ClientRegisterResponse.class);
        return new RegisterResponseWrapper(response.getStatus());
    }

    @Override
    public UpdatedGraphResponseWrapper getUpdatedGraph(int version) {
        HanRoutingProtocol.GraphUpdateRequest graphUpdateRequest = HanRoutingProtocol.GraphUpdateRequest.newBuilder()
                .setCurrentVersion(version).build();
        RequestWrapper req = new RequestWrapper(graphUpdateRequest, HanRoutingProtocol.Wrapper.Type.GRAPHUPDATEREQUEST,
                getSocket());

        HanRoutingProtocol.GraphUpdateResponse response = req
                .writeAndRead(HanRoutingProtocol.GraphUpdateResponse.class);
        UpdatedGraphResponseWrapper updatedGraphs = new UpdatedGraphResponseWrapper(response.getGraphUpdatesList());
        return updatedGraphs;
    }

    @Override
    public ClientGroupResponseWrapper getClientGroup() {
        HanRoutingProtocol.ClientRequest clientRequest = HanRoutingProtocol.ClientRequest.newBuilder().build();
        RequestWrapper req = new RequestWrapper(clientRequest, HanRoutingProtocol.Wrapper.Type.CLIENTREQUEST,
                getSocket());
        HanRoutingProtocol.ClientResponse clientResponse = req.writeAndRead(HanRoutingProtocol.ClientResponse.class);
        return new ClientGroupResponseWrapper(clientResponse.getClientsList());
    }

    /**
     * Returns the connection.
     * @return The connection
     */
    private ConnectionService getConnection() {
        if (isConnectionOpen()) {
            return connectionService;
        }
        startConnection();
        return connectionService;
    }

    /**
     * Starts the connection.
     */
    private void startConnection() {
        if (isConnectionOpen())
            return;
        if (connectionService == null) {
            // new byte[] { 0x00 } = public key that belongs to the cryptography service of the receiver
            //                          en/decryption is disabled for now, so initializing with an null-byte is sufficient.
            connectionService = new ConnectionService(new byte[] { 0x00 });
        }
        try {
            connectionService.open(Configuration.getHostname(), Configuration.getPort());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Checks if the connection is open.
     * @return <tt>true</tt> if the connection is open, <tt>false</tt> if the connection is not.
     */
    private boolean isConnectionOpen() {
        return connectionService != null && connectionService.isConnected();
    }

    /**
     * Closes the connection service.
     */
    public void close() {
        if (isConnectionOpen()) {
            try {
                connectionService.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

}
