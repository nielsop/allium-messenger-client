package nl.han.asd.project.client.commonclient.master;

//import com.xebialabs.overcast.host.CloudHost;

import nl.han.asd.project.client.commonclient.utility.IntegrationTest;
import nl.han.asd.project.client.commonclient.utility.ResponseWrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse;
import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MasterGateway.class)
public class MasterGatewayTest {

}