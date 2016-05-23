package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xebialabs.overcast.host.CloudHost;
import nl.han.asd.project.client.commonclient.Configuration;
import nl.han.asd.project.client.commonclient.connection.ConnectionModule;
import nl.han.asd.project.client.commonclient.connection.IConnectionServiceFactory;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MasterGateway.class)
public class MasterGatewayTest {

    //TODO: Create valuable tests, if possible. All tests that were here, were no IT's, so they are moved.

    private MasterGateway gateway;

    @Before
    public void setup(){
        Injector injector = Guice.createInjector(new ConnectionModule());
        gateway = new MasterGateway(Mockito.mock(Properties.class), injector.getInstance(IConnectionServiceFactory.class));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMasterGatewayGetSocketWithPort65536OutOfRange() {
        gateway.setConnectionData(Configuration.getHostname(), 65536);
        gateway.getSocket();
    }
}
