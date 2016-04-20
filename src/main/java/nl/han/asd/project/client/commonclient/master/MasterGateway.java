package nl.han.asd.project.client.commonclient.master;

import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.client.commonclient.utility.ResponseWrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import javax.inject.Inject;
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
    private IEncrypt encrypt;

    @Inject
    public MasterGateway(IEncrypt encrypt) {
       this.encrypt = encrypt;
    }

        /**
     * @param serverAddress IPv4 address
     * @param serverPort    serverPort to set up for the connection to the master
     */
    public MasterGateway(String serverAddress, int serverPort) {
        validateAddress(serverAddress);
        validatePort(serverPort);

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

    /**
     * @param serverPort Port must be in range 1024 - 65535
     *             You can create a server on ports 1 through 65535.
     *             Port numbers less than 256 are reserved for well-known services (like HTTP on serverPort 80) and serverPort numbers less than 1024 require root access on UNIX systems.
     *             Specifying a serverPort of 0 in the ServerSocket constructor results in the server listening on a random, unused serverPort, usually >= 1024.
     *             http://www.jguru.com/faq/view.jsp?EID=17521
     */
    private void validatePort(int serverPort) {
        if (!(serverPort >= 0 && serverPort <= 65535))
            throw new IllegalArgumentException("Port should be in range of 1024 - 65535.");
    }

    /**
     * Validates the given IP4 address.
     * When the IP4 isn't valid this funtion will throw an error.
     * @param address Address to validate.
     */
    private void validateAddress(String address) {
        //Address may not be null
        if (address == null)
            throw new NullPointerException("Invalid adress; adress may not be null.");
        //IP regular expression
        String ipPattern = "^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$";
        //Create pattern object
        int[] addressAsArray = new int[4];
        Pattern r = Pattern.compile(ipPattern);
        //Create matcher object
        Matcher m = r.matcher(address);
        //Check if match is found
        if (m.find()) {
            for (int i = 1; i < 5; i++) {
                //Parse every group to int
                int ipGroup = Integer.parseInt(m.group(i));
                //Check if first value is not 0.
                if (i == 1 && ipGroup == 0)
                    throw new IllegalArgumentException("First value may not be 0.");
                //Check if at least one group is greater than 254
                if (ipGroup > 254)
                    throw new IllegalArgumentException("One of the IP-values is greater than 254.");
                    //If all values are correct, put the values in an array => [xxx, xxx, xxx, xxx]
                else
                    addressAsArray[i - 1] = ipGroup;
            }
        }
        //No match found
        else {
            throw new IllegalArgumentException("IP format is not valid. Must be xxx.xxx.xxx.xxx");
        }
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
