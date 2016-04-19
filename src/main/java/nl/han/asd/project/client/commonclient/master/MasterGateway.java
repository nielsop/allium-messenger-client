package nl.han.asd.project.client.commonclient.master;

import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.client.commonclient.utility.ResponseWrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MasterGateway implements IGetUpdatedGraph, IGetClients, IHeartbeat {
    //TODO: missing: IWebService from Master
    public IEncrypt encrypt;
    private PrintWriter out;
    private BufferedReader in;
    public Socket socket;

    public MasterGateway(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HanRoutingProtocol.ClientRegisterResponse register(String username, String password) {
        HanRoutingProtocol.ClientRegisterRequest.Builder request = HanRoutingProtocol.ClientRegisterRequest.newBuilder();
        request.setUsername(username).setPassword(password);

        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        final ResponseWrapper response = new ResponseWrapper(HanRoutingProtocol.EncryptedWrapper.Type.CLIENTREGISTERREQUEST, socket);
        return (HanRoutingProtocol.ClientRegisterResponse) response.read();
    }

    public HanRoutingProtocol.ClientLoginResponse login(String username, String password, String publicKey) {
        HanRoutingProtocol.ClientLoginRequest.Builder request = HanRoutingProtocol.ClientLoginRequest.newBuilder();
        request.setUsername(username).setPassword(password).setPublicKey(publicKey);

        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        final ResponseWrapper response = new ResponseWrapper(HanRoutingProtocol.EncryptedWrapper.Type.CLIENTLOGINREQUEST, socket);
        return (HanRoutingProtocol.ClientLoginResponse) response.read();
    }

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
