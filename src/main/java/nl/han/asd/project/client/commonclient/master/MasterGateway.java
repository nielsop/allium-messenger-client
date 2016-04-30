package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Inject;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.master.wrapper.ClientGroupResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.UpdatedGraphResponseWrapper;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Base64;

public class MasterGateway implements IGetUpdatedGraph, IGetClientGroup, IRegistration, IHeartbeat, IAuthentication {

    //TODO: missing: IWebService from Master

    private static int currentGraphVersion = -1;
    private ConnectionService connectionService;
    private Socket socket;
    private String hostname;
    private int port;

    private IEncryptionService encryptionService;

    @Inject
    public MasterGateway(String hostname, int port, IEncryptionService encryptionService) {
        this.hostname = hostname;
        this.port = port;
        this.encryptionService = encryptionService;

        try {
            socket = new Socket(hostname, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LoginResponseWrapper authenticate(String username, String password) {
        HanRoutingProtocol.ClientLoginRequest loginRequest = HanRoutingProtocol.ClientLoginRequest.newBuilder()
                .setUsername(username).setPassword(password).setPublicKey(getPublicKey()).build();
        RequestWrapper request = new RequestWrapper(loginRequest, HanRoutingProtocol.EncryptedWrapper.Type.CLIENTLOGINREQUEST, socket);
        HanRoutingProtocol.ClientLoginResponse response = request.writeAndRead(HanRoutingProtocol.ClientLoginResponse.class);
        return new LoginResponseWrapper(response.getConnectedNodesList(), response.getSecretHash(),
                response.getStatus());
    }

    @Override
    public RegisterResponseWrapper register(String username, String password) {
        HanRoutingProtocol.ClientRegisterRequest registerRequest = HanRoutingProtocol.ClientRegisterRequest.newBuilder()
                .setUsername(username).setPassword(password).build();
        RequestWrapper req = new RequestWrapper(registerRequest, HanRoutingProtocol.EncryptedWrapper.Type.CLIENTREGISTERREQUEST, socket);
        HanRoutingProtocol.ClientRegisterResponse response = req.writeAndRead(HanRoutingProtocol.ClientRegisterResponse.class);
        return new RegisterResponseWrapper(response.getStatus());
    }

    @Override
    public UpdatedGraphResponseWrapper getUpdatedGraph() {
        HanRoutingProtocol.GraphUpdateRequest graphUpdateRequest = HanRoutingProtocol.GraphUpdateRequest.newBuilder()
                .setCurrentVersion(getCurrentGraphVersion()).build();
        RequestWrapper req = new RequestWrapper(graphUpdateRequest, HanRoutingProtocol.EncryptedWrapper.Type.GRAPHUPDATEREQUEST, socket);

        HanRoutingProtocol.GraphUpdateResponse response = req.writeAndRead(HanRoutingProtocol.GraphUpdateResponse.class);
        UpdatedGraphResponseWrapper updatedGraphs = new UpdatedGraphResponseWrapper(response.getGraphUpdatesList());
        setCurrentGraphVersion(updatedGraphs.getLast().newVersion);
        return updatedGraphs;
    }

    @Override
    public ClientGroupResponseWrapper getClientGroup() {
        HanRoutingProtocol.ClientRequest clientRequest = HanRoutingProtocol.ClientRequest.newBuilder().build();
        RequestWrapper req = new RequestWrapper(clientRequest, HanRoutingProtocol.EncryptedWrapper.Type.CLIENTREQUEST, socket);
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
        if (isConnectionOpen()) return;
        if (connectionService == null) {
            connectionService = new ConnectionService();
        }
        try {
            connectionService.open(hostname, port);
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
}
