package nl.han.asd.project.client.commonclient.connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created by Jevgeni on 22-4-2016.
 */
public class ConnectionTest {

    private Connection connection = null;

    @Before
    public void Init() {
        connection = new Connection();
    }

    @After
    public void DeInit() throws IOException {
        connection.close();
    }

    @Test(expected = SocketException.class)
    public void TestClosedRead() throws IOException {
        connection.close();
        connection.read();
    }

}
