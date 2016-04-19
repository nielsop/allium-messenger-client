package nl.han.asd.project.client.commonclient.master;

import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.registration.IRegistration;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.client.commonclient.utility.ResponseWrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MasterGateway implements IGetUpdatedGraph, IGetClients, IHeartbeat, IRegistration {
    //TODO: missing: IWebService from Master
    public IEncrypt encrypt;
    public Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String address;
    private int port;

    /**
     * @param address IPv4 address
     * @param port    port to set up for the connection to the master
     */
    public MasterGateway(String address, int port) {
        validateAddress(address);
        validatePort(port);

        this.address = address;
        this.port = port;
        try {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param port Port must be in range 1024 - 65535
     *             You can create a server on ports 1 through 65535.
     *             Port numbers less than 256 are reserved for well-known services (like HTTP on port 80) and port numbers less than 1024 require root access on UNIX systems.
     *             Specifying a port of 0 in the ServerSocket constructor results in the server listening on a random, unused port, usually >= 1024.
     *             http://www.jguru.com/faq/view.jsp?EID=17521
     */
    private void validatePort(int port) {
        if (!(port >= 0 && port <= 65535))
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

        final ResponseWrapper response = new ResponseWrapper(ResponseWrapper.ResponseType.CLIENT_REGISTRATION, socket);
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

        final ResponseWrapper response = new ResponseWrapper(ResponseWrapper.ResponseType.CLIENT_LOGIN, socket);
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
        HanRoutingProtocol.ClientRegisterRequest.Builder requestBuilder = HanRoutingProtocol.ClientRegisterRequest.newBuilder();
        requestBuilder.setPassword(password).setUsername(username);
        ByteString data = requestBuilder.build().toByteString();

        HanRoutingProtocol.EncryptedWrapper.Builder builder = HanRoutingProtocol.EncryptedWrapper.newBuilder();
        builder.setType(HanRoutingProtocol.EncryptedWrapper.Type.CLIENTREGISTERREQUEST).setPublicKey(key).setData(data);

        try {
            builder.build().writeDelimitedTo(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            HanRoutingProtocol.EncryptedWrapper registerResponse;
            while ((registerResponse = HanRoutingProtocol.EncryptedWrapper.parseFrom(socket.getInputStream())) != null) {
                return HanRoutingProtocol.NodeRegisterResponse.parseFrom(registerResponse.getData());
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
