package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Inject;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.Configuration;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.master.wrapper.ClientGroupResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.UpdatedGraphResponseWrapper;
import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.net.SocketException;

public class MasterGateway implements IGetUpdatedGraph, IGetClientGroup, IRegistration, IHeartbeat, IAuthentication {

    //TODO: missing: IWebService from Master

    private static int currentGraphVersion = -1;
    private ConnectionService connectionService;

    private IEncryptionService encryptionService;
    private String publicKey;

    @Inject
    public MasterGateway(IEncryptionService encryptionService) {
        Validation.validateAddress(Configuration.HOSTNAME);
        Validation.validatePort(Configuration.PORT);
        this.encryptionService = encryptionService;
        connectionService = new ConnectionService();
        try {
            connectionService.open(Configuration.HOSTNAME,
                    Configuration.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public LoginResponseWrapper authenticate(String username, String password) {
        HanRoutingProtocol.ClientLoginRequest loginRequest = HanRoutingProtocol.ClientLoginRequest.newBuilder()
                .setUsername(username).setPassword(password).setPublicKey(getPublicKey()).build();

        HanRoutingProtocol.ClientLoginResponse loginResponse = writeAndRead(HanRoutingProtocol.ClientLoginResponse.class,
                loginRequest.toByteArray());
        if (loginResponse == null) return null;
        return new LoginResponseWrapper(loginResponse.getConnectedNodesList(), loginResponse.getSecretHash(),
                loginResponse.getStatus());
    }

    @Override
    public RegisterResponseWrapper register(String username, String password) {
        HanRoutingProtocol.ClientLoginRequest registerRequest = HanRoutingProtocol.ClientLoginRequest.newBuilder()
                .setUsername(username).setPassword(password).setPublicKey("ayy").build();
        HanRoutingProtocol.EncryptedWrapper encryptedRequest = HanRoutingProtocol.EncryptedWrapper.newBuilder()
                .setData(registerRequest.toByteString()).setType(HanRoutingProtocol.EncryptedWrapper.Type.CLIENTREGISTERREQUEST).build();

        HanRoutingProtocol.ClientRegisterResponse registerResponse = writeAndRead(HanRoutingProtocol.ClientRegisterResponse.class,
                encryptedRequest.toByteArray());
        if (registerResponse == null) return null;
        return new RegisterResponseWrapper(registerResponse.getStatus());
    }

    @Override
    public UpdatedGraphResponseWrapper getUpdatedGraph(int currentVersion) {
        HanRoutingProtocol.GraphUpdateRequest graphUpdateRequest = HanRoutingProtocol.GraphUpdateRequest.newBuilder()
                .setCurrentVersion(currentVersion).build();

        HanRoutingProtocol.EncryptedWrapper encryptedRequest = HanRoutingProtocol.EncryptedWrapper.newBuilder()
                .setData(graphUpdateRequest.toByteString()).setType(HanRoutingProtocol.EncryptedWrapper.Type.GRAPHUPDATEREQUEST).build();


        HanRoutingProtocol.GraphUpdateResponse graphUpdate = writeAndRead(HanRoutingProtocol.GraphUpdateResponse.class, encryptedRequest.toByteArray());
        if (graphUpdate == null) return null;

        return new UpdatedGraphResponseWrapper(graphUpdate.getGraphUpdatesList());
    }

    @Override
    public ClientGroupResponseWrapper getClientGroup() {
        HanRoutingProtocol.ClientRequest clientRequest = HanRoutingProtocol.ClientRequest.newBuilder().build();

        HanRoutingProtocol.ClientResponse clientResponse = writeAndRead(HanRoutingProtocol.ClientResponse.class,
                clientRequest.toByteArray());
        if (clientResponse == null) return null;
        return new ClientGroupResponseWrapper(clientResponse.getClientsList());
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
//    private String getPublicKey() {
//        return Base64.getEncoder().encodeToString(encryptionService.getPublicKey().getEncoded());
//    }


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
        if (isConnectionOpen()) return;
        if (connectionService == null) {
            connectionService = new ConnectionService();
        }
        try {
            connectionService.open(Configuration.HOSTNAME, Configuration.PORT);
        } catch (IOException ioe) {
            ioe.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }


    /**
     * Writes a byte array to the connection and parses the response.
     *
     * @param classDescriptor The class to parse the response to.
     * @param data            The byte array with data.
     * @param <T>             Type of the class to parse the response to.
     * @return A parsed response.
     */
    private <T extends GeneratedMessage> T writeAndRead(Class<T> classDescriptor, byte[] data) {
        final ConnectionService connection = getConnection();
        try {
            connection.write(data);
            return connectionService.readGeneric(classDescriptor);
        } catch (SocketException | InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
