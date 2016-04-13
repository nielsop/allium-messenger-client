package nl.han.onionmessenger.commonclient.registration;

import nl.han.onionmessenger.commonclient.master.MasterGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Kan later weggegooid worden.
 */
@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest {

    @InjectMocks
    private RegistrationService sut;

    @Mock
    private MasterGateway masterGatewayMock;

    @Test
    public void testRegistrationOnMasterGateway() throws Exception
    {
        String registrationData = "";

        //Mock
        when(masterGatewayMock.registerClient(registrationData)).thenReturn("success");

        assertEquals("success", masterGatewayMock.registerClient(registrationData));
    }

    /*//Nog niet boeiend
    @Test
    public void testDependenciesNotNull() throws Exception {
        sut = new RegistrationService();
        assertNotNull(sut.masterGateway);
    }*/

}