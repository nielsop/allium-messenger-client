package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.client.commonclient.utility.ResponseWrapper;
import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import sun.java2d.pipe.ValidatePipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MasterGateway implements IGetUpdatedGraph, IGetClientGroup, IRegistration, IHeartbeat, IAuthentication {
    //TODO: missing: IWebService from Master
    public Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String serverAddress;
    private int serverPort;
    private IEncrypt encrypt; //TODO: Private of public? Stond verderop namelijk nog een keer, maar dan public.

    @Inject
    public MasterGateway(IEncrypt encrypt) {
       this.encrypt = encrypt;
    }

        /**
     * @param serverAddress IPv4 address
     * @param serverPort    serverPort to set up for the connection to the master
     */
    public MasterGateway(String serverAddress, int serverPort) {
        final Validation validation = new Validation();
        validation.validateAddress(serverAddress);
        validation.validatePort(serverPort);

        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HanRoutingProtocol.ClientLoginResponse authenticateUser(final String username, final String password,
                                                                   final String publicKey) throws IOException {
        /* Code to prevent merge conflicts until we get dependency injection */
        Socket socket = null;
        try {
            socket = new Socket("localhost", 1234);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (socket != null) {
            final HanRoutingProtocol.ClientLoginRequest.Builder loginRequestBuilder = HanRoutingProtocol.ClientLoginRequest.
                    newBuilder();
            loginRequestBuilder.setUsername(username).setPassword(password).setPublicKey(publicKey);

            final RequestWrapper loginRequest = new RequestWrapper(loginRequestBuilder.build(), socket);
            loginRequest.writeToSocket();

            final ResponseWrapper loginResponse = new ResponseWrapper(HanRoutingProtocol.EncryptedWrapper.Type.CLIENTLOGINRESPONSE, socket);
            return (HanRoutingProtocol.ClientLoginResponse) loginResponse.read();
        }
        return null;
    }
	
    public HanRoutingProtocol.ClientRegisterResponse register(String username, String password) {
        HanRoutingProtocol.ClientRegisterRequest.Builder request = HanRoutingProtocol.ClientRegisterRequest.newBuilder();
        request.setUsername(username).setPassword(password);

        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        final ResponseWrapper response = new ResponseWrapper(HanRoutingProtocol.EncryptedWrapper.Type.CLIENTLOGINRESPONSE, socket);
        try {
            return (HanRoutingProtocol.ClientRegisterResponse) response.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.ClientLoginResponse login(String username, String password, String publicKey) throws IOException {
        HanRoutingProtocol.ClientLoginRequest.Builder request = HanRoutingProtocol.ClientLoginRequest.newBuilder();
        request.setUsername(username).setPassword(password).setPublicKey(publicKey);

        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        final ResponseWrapper response = new ResponseWrapper(HanRoutingProtocol.EncryptedWrapper.Type.CLIENTLOGINREQUEST, socket);
        return (HanRoutingProtocol.ClientLoginResponse) response.read();
    }

    /* ^ Bovenstaand zijn volgens nieuwe wrappers voor response/request */

    public void write(String data) {
        out.println(data);
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    public HanRoutingProtocol.ClientRegisterResponse testRegisterClient(String password, String username, String key) {
        HanRoutingProtocol.ClientRegisterRequest.Builder builder = HanRoutingProtocol.ClientRegisterRequest.newBuilder();
        builder.setPassword(password).setUsername(username);
        ByteString data = builder.build().toByteString();

        HanRoutingProtocol.EncryptedWrapper.Builder request = HanRoutingProtocol.EncryptedWrapper.newBuilder();
        request.setType(HanRoutingProtocol.EncryptedWrapper.Type.CLIENTREGISTERREQUEST).setPublicKey(key).setData(data);
        System.out.println(request.build());
        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        try {
            HanRoutingProtocol.EncryptedWrapper registerResponse;
            while ((registerResponse = HanRoutingProtocol.EncryptedWrapper.parseFrom(socket.getInputStream())) != null) {
                return HanRoutingProtocol.ClientRegisterResponse.parseFrom(registerResponse.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.NodeRegisterResponse testRegisterNode(String ip, int port, String key) {
        HanRoutingProtocol.NodeRegisterRequest.Builder builder = HanRoutingProtocol.NodeRegisterRequest.newBuilder();
        builder.setIPaddress(ip).setPort(port).setPublicKey(key);
        ByteString data = builder.build().toByteString();

        HanRoutingProtocol.EncryptedWrapper.Builder request = HanRoutingProtocol.EncryptedWrapper.newBuilder();
        request.setType(HanRoutingProtocol.EncryptedWrapper.Type.NODEREGISTERREQUEST).setPublicKey(key).setData(data);

        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        try {
            HanRoutingProtocol.EncryptedWrapper registerResponse;
            while ((registerResponse = HanRoutingProtocol.EncryptedWrapper.parseFrom(socket.getInputStream())) != null) {
                return HanRoutingProtocol.NodeRegisterResponse.parseFrom(registerResponse.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.NodeUpdateResponse testUpdateNode(String id, String secretHash, String ip, int port, String key) {
        HanRoutingProtocol.NodeUpdateRequest.Builder builder = HanRoutingProtocol.NodeUpdateRequest.newBuilder();
        builder.setId(id).setSecretHash(secretHash).setIPaddress(ip).setPort(port);
        ByteString data = builder.build().toByteString();

        HanRoutingProtocol.EncryptedWrapper.Builder request = HanRoutingProtocol.EncryptedWrapper.newBuilder();
        request.setType(HanRoutingProtocol.EncryptedWrapper.Type.NODEUPDATEREQUEST).setPublicKey(key).setData(data);

        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        try {
            HanRoutingProtocol.EncryptedWrapper updateResponse;
            while ((updateResponse = HanRoutingProtocol.EncryptedWrapper.parseFrom(socket.getInputStream())) != null) {
                return HanRoutingProtocol.NodeUpdateResponse.parseFrom(updateResponse.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.NodeDeleteResponse testDeleteNode(String id, String secretHash, String key) {
        HanRoutingProtocol.NodeDeleteRequest.Builder builder = HanRoutingProtocol.NodeDeleteRequest.newBuilder();
        builder.setId(id).setSecretHash(secretHash);
        ByteString data = builder.build().toByteString();

        HanRoutingProtocol.EncryptedWrapper.Builder request = HanRoutingProtocol.EncryptedWrapper.newBuilder();
        request.setType(HanRoutingProtocol.EncryptedWrapper.Type.NODEDELETEREQUEST).setPublicKey(key).setData(data);

        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        try {
            HanRoutingProtocol.EncryptedWrapper deleteResponse;
            while ((deleteResponse = HanRoutingProtocol.EncryptedWrapper.parseFrom(socket.getInputStream())) != null) {
                return HanRoutingProtocol.NodeDeleteResponse.parseFrom(deleteResponse.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.ClientResponse testGetClients(int clientGroup, String key) {
        HanRoutingProtocol.ClientRequest.Builder builder = HanRoutingProtocol.ClientRequest.newBuilder();
        builder.setClientGroup(clientGroup);
        ByteString data = builder.build().toByteString();

        HanRoutingProtocol.EncryptedWrapper.Builder request = HanRoutingProtocol.EncryptedWrapper.newBuilder();
        request.setType(HanRoutingProtocol.EncryptedWrapper.Type.CLIENTREQUEST).setPublicKey(key).setData(data);

        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        try {
            HanRoutingProtocol.EncryptedWrapper clientResponse;
            while ((clientResponse = HanRoutingProtocol.EncryptedWrapper.parseFrom(socket.getInputStream())) != null) {
                return HanRoutingProtocol.ClientResponse.parseFrom(clientResponse.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.GraphUpdateResponse testGraphUpdate(int version, String key) {
        HanRoutingProtocol.GraphUpdateRequest.Builder builder = HanRoutingProtocol.GraphUpdateRequest.newBuilder();
        builder.setCurrentVersion(version);
        ByteString data = builder.build().toByteString();

        HanRoutingProtocol.EncryptedWrapper.Builder request = HanRoutingProtocol.EncryptedWrapper.newBuilder();
        request.setType(HanRoutingProtocol.EncryptedWrapper.Type.GRAPHUPDATEREQUEST).setPublicKey(key).setData(data);

        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        try {
            HanRoutingProtocol.EncryptedWrapper graphResponse;
            while ((graphResponse = HanRoutingProtocol.EncryptedWrapper.parseFrom(socket.getInputStream())) != null) {
                return HanRoutingProtocol.GraphUpdateResponse.parseFrom(graphResponse.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.Message test() {
        HanRoutingProtocol.Message.Builder builder = HanRoutingProtocol.Message.newBuilder();
        builder.setId("1235ab");
        builder.setSender("Raoul");
        builder.setText("Dit is de tekst! Goeie shit, goeie shit.");

        try {
            builder.build().writeDelimitedTo(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            HanRoutingProtocol.Message message;
            while ((message = HanRoutingProtocol.Message.parseFrom(socket.getInputStream())) != null) {
                return message;
            }
        } catch (IOException e) {
            e.printStackTrace();
		}
        return null;
	}
}
