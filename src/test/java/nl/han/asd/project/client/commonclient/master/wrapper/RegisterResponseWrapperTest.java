package nl.han.asd.project.client.commonclient.master.wrapper;

import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 29-4-2016
 */
public class RegisterResponseWrapperTest {

    @Test
    public void testRegisterWrapperCreationSavesStatus() {
        final RegisterResponseWrapper registerResponseWrapper = new RegisterResponseWrapper(
                HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES);
        Assert.assertEquals(registerResponseWrapper.getStatus(), HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES);
    }
}