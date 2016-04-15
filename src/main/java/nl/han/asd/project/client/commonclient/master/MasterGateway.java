package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.client.commonclient.utility.ResponseWrapper;
import nl.han.onionmessenger.commonclient.HanRoutingProtocol;

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

        final ResponseWrapper response = new ResponseWrapper(ResponseWrapper.ResponseType.CLIENT_REGISTRATION, socket);
        return (HanRoutingProtocol.ClientRegisterResponse) response.read();
    }

    public HanRoutingProtocol.ClientLoginResponse login(String username, String password, String publicKey) {
        HanRoutingProtocol.ClientLoginRequest.Builder request = HanRoutingProtocol.ClientLoginRequest.newBuilder();
        request.setUsername(username).setPassword(password).setPublicKey(publicKey);

        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        final ResponseWrapper response = new ResponseWrapper(ResponseWrapper.ResponseType.CLIENT_LOGIN, socket);
        return (HanRoutingProtocol.ClientLoginResponse) response.read();
    }

    public void write(String data) {
        out.println(data);
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    public HanRoutingProtocol.ClientRegisterResponse testRegisterClient() {
        HanRoutingProtocol.ClientRegisterRequest.Builder builder = HanRoutingProtocol.ClientRegisterRequest.newBuilder();
        builder.setPassword("banaan");
        builder.setUsername("Koen");

        try {
            builder.build().writeDelimitedTo(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        HanRoutingProtocol.ClientRegisterResponse registerResponse;
        try {
            while ((registerResponse = HanRoutingProtocol.ClientRegisterResponse.parseFrom(socket.getInputStream())) != null) {
                return registerResponse;
                //return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.NodeRegisterResponse testRegisterNode() {
        HanRoutingProtocol.NodeRegisterRequest.Builder builder = HanRoutingProtocol.NodeRegisterRequest.newBuilder();
        builder.setIPaddress("192.168.0.0");
        builder.setPort(80);
        builder.setPublicKey("12345abcde");

        try {
            builder.build().writeDelimitedTo(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            HanRoutingProtocol.NodeRegisterResponse registerResponse;
            while ((registerResponse = HanRoutingProtocol.NodeRegisterResponse.parseFrom(socket.getInputStream())) != null) {
                return registerResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.NodeUpdateResponse testUpdateNode() {
        HanRoutingProtocol.NodeUpdateRequest.Builder builder = HanRoutingProtocol.NodeUpdateRequest.newBuilder();
        builder.setId("2");
        builder.setSecretHash("56857cfc709d3996f057252c16ec4656f5292802");
        builder.setIPaddress("192.170.0.1");
        builder.setPort(90);

        try {
            builder.build().writeDelimitedTo(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            HanRoutingProtocol.NodeUpdateResponse updateResponse;
            while ((updateResponse = HanRoutingProtocol.NodeUpdateResponse.parseFrom(socket.getInputStream())) != null) {
                return updateResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.NodeDeleteResponse testDeleteNode() {
        HanRoutingProtocol.NodeDeleteRequest.Builder builder = HanRoutingProtocol.NodeDeleteRequest.newBuilder();
        builder.setId("2");
        builder.setSecretHash("56857cfc709d3996f057252c16ec4656f5292802");

        try {
            builder.build().writeDelimitedTo(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            HanRoutingProtocol.NodeDeleteResponse deleteResponse;
            while ((deleteResponse = HanRoutingProtocol.NodeDeleteResponse.parseFrom(socket.getInputStream())) != null) {
                return deleteResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.ClientResponse testGetClients() {
        HanRoutingProtocol.ClientRequest.Builder builder = HanRoutingProtocol.ClientRequest.newBuilder();
        builder.setClientGroup(10);

        try {
            builder.build().writeDelimitedTo(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            HanRoutingProtocol.ClientResponse clientResponse;
            while ((clientResponse = HanRoutingProtocol.ClientResponse.parseFrom(socket.getInputStream())) != null) {
                return clientResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HanRoutingProtocol.GraphUpdateResponse testGraphUpdate() {
        HanRoutingProtocol.GraphUpdateRequest.Builder builder = HanRoutingProtocol.GraphUpdateRequest.newBuilder();
        builder.setCurrentVersion(10);

        try {
            builder.build().writeDelimitedTo(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            HanRoutingProtocol.GraphUpdateResponse graphUpdateResponse;
            while ((graphUpdateResponse = HanRoutingProtocol.GraphUpdateResponse.parseFrom(socket.getInputStream())) != null) {
                return graphUpdateResponse;
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
