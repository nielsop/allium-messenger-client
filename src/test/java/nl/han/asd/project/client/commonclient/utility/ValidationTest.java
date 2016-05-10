package nl.han.asd.project.client.commonclient.utility;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bram Heijmink on 10-5-2016.
 */
public class ValidationTest {

    @Test
    public void TestValidIPV4Address(){
        assertEquals(Validation.validateAddress("64.233.161.147"),true);
    }

    @Test
    public void TestValidIPV6AddressLong(){
        assertEquals(Validation.validateAddress("2001:cdba:0000:0000:0000:0000:3257:9652"),true);
    }

    @Test
    public void TestValidIPV6AddressSingleZeroGroup(){
        assertEquals(Validation.validateAddress("2001:cdba:0:0:0:0:3257:9652"),true);
    }

    @Test
    public void TestValidIPV6AddressNoZeroGroup(){
        assertEquals(Validation.validateAddress("2001:cdba::3257:9652"),true);
    }

    @Test
    public void TestInvalidIPAddress(){
        assertEquals(Validation.validateAddress("df.34.23.23fsd"),false);
    }
}
