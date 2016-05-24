package nl.han.asd.project.client.commonclient.master;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.Test;

import nl.han.asd.project.client.commonclient.connection.IConnectionService;
import nl.han.asd.project.client.commonclient.connection.IConnectionServiceFactory;

public class MasterGatewayTest {
    @Test
    public void testName() throws Exception {
        String host = "localhost";
        int port = 1024;

        Properties properties = new Properties();
        properties.setProperty("master-server-host", host);
        properties.setProperty("master-server-port", Integer.toString(port));

        IConnectionService connectionServiceMock = mock(IConnectionService.class);

        IConnectionServiceFactory connectionServiceFactoryMock = mock(IConnectionServiceFactory.class);
        when(connectionServiceFactoryMock.create(eq(host), eq(port))).thenReturn(connectionServiceMock);

        new MasterGateway(properties, connectionServiceFactoryMock);

        verify(connectionServiceFactoryMock).create(eq(host), eq(port));
    }
}
