package nl.han.asd.project.client.commonclient.testtest;

import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.connection.Connection;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketException;

import static org.junit.Assert.assertEquals;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 22-4-2016
 */
public class TestTest {

    private ConnectionService connection;

    @Before
    public void setUp() {
        connection = new ConnectionService();
        try {
            connection.open("195.169.194.234", 32851);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void doTest() {
        HanRoutingProtocol.ClientLoginRequest.Builder loginRequestBuilder = HanRoutingProtocol.ClientLoginRequest.newBuilder();
        loginRequestBuilder.setUsername("nielsje41");
        loginRequestBuilder.setPassword("wachtwoord");
        loginRequestBuilder.setPublicKey("123456789");
        try {
            connection.writeGeneric(loginRequestBuilder);
            HanRoutingProtocol.ClientLoginResponse response = connection.readGeneric(HanRoutingProtocol.ClientLoginResponse.class);
            assertEquals(HanRoutingProtocol.ClientLoginResponse.Status.SUCCES, response.getStatus());
        } catch (SocketException | InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
