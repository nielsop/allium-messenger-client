package nl.han.asd.project.client.commonclient.master;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.Configuration;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import javax.inject.Inject;
import java.io.IOException;
import java.net.SocketException;
import java.util.Base64;

public class MasterGateway implements IGetUpdatedGraph, IGetClientGroup, IRegistration, IHeartbeat, IAuthentication {

    //TODO: missing: IWebService from Master

    private ConnectionService connectionService;
    private IEncryptionService encryptionService;

    @Inject
    public MasterGateway(IEncryptionService encryptionService) {
        Validation.validateAddress(Configuration.HOSTNAME);
        Validation.validatePort(Configuration.PORT);
        this.encryptionService = encryptionService;
    }

    @Override
    public LoginResponseWrapper authenticate(String username, String password) {
        if (Validation.validateLoginData(username, password)) {
            HanRoutingProtocol.ClientLoginRequest loginRequest = HanRoutingProtocol.ClientLoginRequest.newBuilder()
                    .setUsername(username).setPassword(password).setPublicKey(getPublicKey()).build();

            HanRoutingProtocol.ClientLoginResponse loginResponse = writeAndRead(HanRoutingProtocol.ClientLoginResponse.class,
                    loginRequest.toByteArray());
            if (loginResponse == null) return null;
            return new LoginResponseWrapper(loginResponse.getConnectedNodesList(), loginResponse.getSecretHash(),
                    loginResponse.getStatus());
        }
        return null;
    }

    @Override
    public RegisterResponseWrapper register(String username, String password) {

        HanRoutingProtocol.ClientRegisterRequest registerRequest = HanRoutingProtocol.ClientRegisterRequest.newBuilder()
                .setUsername(username).setPassword(password).build();

        HanRoutingProtocol.EncryptedWrapper encryptedWrapper = HanRoutingProtocol.EncryptedWrapper.newBuilder()
                .setData(registerRequest.toByteString()).setType(
                        HanRoutingProtocol.EncryptedWrapper.Type.CLIENTREGISTERREQUEST).build();

        HanRoutingProtocol.ClientRegisterResponse registerResponse = writeAndRead(HanRoutingProtocol.ClientRegisterResponse.class,
                encryptedWrapper.toByteArray());
        if(registerResponse == null){
            System.out.println("Register response is null");
        }
        if (registerResponse == null) return null;

        return new RegisterResponseWrapper(registerResponse.getStatus());
    }

    /**
     * Returns the public key.
     *
     * @return The public key.
     */
    private String getPublicKey() {
        return "";//Base64.getEncoder().encodeToString(encryptionService.getPublicKey());
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

}
