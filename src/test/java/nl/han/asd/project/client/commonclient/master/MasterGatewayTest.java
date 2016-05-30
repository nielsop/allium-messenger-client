package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.Configuration;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.connection.IConnectionService;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hsqldb.lib.StringConverter.hexStringToByteArray;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MasterGateway.class)
public class MasterGatewayTest {

    byte[] byteArray;
    private MasterGateway gateway;

    @Before
    public void setup() throws Exception {
        Injector injector = Guice.createInjector(new EncryptionModule());
        gateway = new MasterGateway(injector.getInstance(IEncryptionService.class));
        byteArray = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMasterGatewayGetSocketWithPort65536OutOfRange() {
        gateway.setConnectionData(Configuration.getHostname(), 65536);
        gateway.getSocket();
    }

    @Test
    public void testGetConnectionActuallyGetsTheSetConnection() throws Exception {
        ConnectionService actualConnService = new ConnectionService(25, byteArray, PowerMockito.mock(IConnectionService.class));
        setPrivateConnectionService(actualConnService);
        ConnectionService returnedConnService = getPrivateConnectionService();
        Assert.assertEquals(actualConnService, returnedConnService);
    }

    private ConnectionService getPrivateConnectionService() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method getConnection = gateway.getClass().getDeclaredMethod("getConnection");
        getConnection.setAccessible(true);
        ConnectionService returnedConnService = (ConnectionService) getConnection.invoke(gateway);
        getConnection.setAccessible(false);
        return returnedConnService;
    }

    private void setPrivateConnectionService(ConnectionService actualConnService) throws NoSuchFieldException, IllegalAccessException {
        Field connService = gateway.getClass().getDeclaredField("connectionService");
        connService.setAccessible(true);
        connService.set(gateway, actualConnService);
        connService.setAccessible(false);
    }
}
