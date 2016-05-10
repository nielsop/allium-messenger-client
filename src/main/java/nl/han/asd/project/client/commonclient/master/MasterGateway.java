package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.Configuration;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.master.wrapper.ClientGroupResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.UpdatedGraphResponseWrapper;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Base64;

public class MasterGateway implements IGetUpdatedGraph, IGetClientGroup, IRegistration, IHeartbeat, IAuthentication {
    //TODO: missing: IWebService from Master

    private static int currentGraphVersion = -1;
    private ConnectionService connectionService;
    private Socket socket;
    private IEncryptionService encryptionService;
    public static final Logger logger = LoggerFactory.getLogger(MasterGateway.class);
    private String hostname = Configuration.HOSTNAME;
    private int port = Configuration.PORT;

    @Inject
    public MasterGateway(IEncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public void setConnectionData(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public Socket getSocket() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket(hostname, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }

    @Override
    public LoginResponseWrapper authenticate(String username, String password) {
        HanRoutingProtocol.ClientLoginRequest loginRequest = HanRoutingProtocol.ClientLoginRequest.newBuilder()
                .setUsername(username).setPassword(password).setPublicKey(getPublicKey()).build();
        RequestWrapper request = new RequestWrapper(loginRequest, HanRoutingProtocol.Wrapper.Type.CLIENTLOGINREQUEST, getSocket());
        HanRoutingProtocol.ClientLoginResponse response = request.writeAndRead(HanRoutingProtocol.ClientLoginResponse.class);
        return new LoginResponseWrapper(response.getConnectedNodesList(), response.getSecretHash(), response.getStatus());
    }

    @Override
    public RegisterResponseWrapper register(String username, String password) {
        HanRoutingProtocol.ClientRegisterRequest registerRequest = HanRoutingProtocol.ClientRegisterRequest.newBuilder().setUsername(username).setPassword(password).build();
        RequestWrapper req = new RequestWrapper(registerRequest, HanRoutingProtocol.Wrapper.Type.CLIENTREGISTERREQUEST, getSocket());
        HanRoutingProtocol.ClientRegisterResponse response = req.writeAndRead(HanRoutingProtocol.ClientRegisterResponse.class);
        return new RegisterResponseWrapper(response.getStatus());
    }

    @Override
    public UpdatedGraphResponseWrapper getUpdatedGraph(int version) {
        HanRoutingProtocol.GraphUpdateRequest graphUpdateRequest = HanRoutingProtocol.GraphUpdateRequest.newBuilder().setCurrentVersion(version).build();
        RequestWrapper req = new RequestWrapper(graphUpdateRequest, HanRoutingProtocol.Wrapper.Type.GRAPHUPDATEREQUEST, getSocket());

        HanRoutingProtocol.GraphUpdateResponse response = req.writeAndRead(HanRoutingProtocol.GraphUpdateResponse.class);
        UpdatedGraphResponseWrapper updatedGraphs = new UpdatedGraphResponseWrapper(response.getGraphUpdatesList());
        setCurrentGraphVersion(updatedGraphs.getLast().newVersion);
        return updatedGraphs;
    }

    @Override
    public ClientGroupResponseWrapper getClientGroup() {
        HanRoutingProtocol.ClientRequest clientRequest = HanRoutingProtocol.ClientRequest.newBuilder().build();
        RequestWrapper req = new RequestWrapper(clientRequest, HanRoutingProtocol.Wrapper.Type.CLIENTREQUEST, getSocket());
        HanRoutingProtocol.ClientResponse clientResponse = req.writeAndRead(HanRoutingProtocol.ClientResponse.class);
        return new ClientGroupResponseWrapper(clientResponse.getClientsList());
    }

    /**
     * Returns the current graph version.
     *
     * @return The current graph version.
     */
    public int getCurrentGraphVersion() {
        return currentGraphVersion;
    }

    /**
     * Sets the current graph version.
     *
     * @param newVersion The new graph version.
     */
    private void setCurrentGraphVersion(int newVersion) {
        currentGraphVersion = newVersion;
    }

    /**
     * Returns the public key.
     *
     * @return The public key.
     */
    private String getPublicKey() {
        return Base64.getEncoder().encodeToString(encryptionService.getPublicKey());
    }

    /**
     * Returns the connection.
     *
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
            connectionService.open(Configuration.HOSTNAME, Configuration.PORT);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Checks if the connection is open.
     *
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
                logger.error(e.getMessage(), e);
            }
        }
    }

}
