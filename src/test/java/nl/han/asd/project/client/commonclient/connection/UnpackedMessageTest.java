package nl.han.asd.project.client.commonclient.connection;

import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Jevgeni on 17-5-2016.
 */
public class UnpackedMessageTest {

    @Test
    public void testMatchingMethods()
    {
        HanRoutingProtocol.Message message = getMessage();
        UnpackedMessage unpackedMessage = new UnpackedMessage(message.toByteArray(),
                HanRoutingProtocol.Wrapper.Type.MESSAGE, message);

        Assert.assertEquals(true, unpackedMessage.matchDataMessage(HanRoutingProtocol.Message.class));
        Assert.assertEquals(true, unpackedMessage.matchDataType(
                HanRoutingProtocol.Wrapper.Type.MESSAGE));
    }


    private HanRoutingProtocol.Message getMessage() {
        HanRoutingProtocol.Message.Builder messageBuilder = HanRoutingProtocol.Message.newBuilder();
        messageBuilder.setId("2222222");
        messageBuilder.setSender("Bob");
        messageBuilder.setText("Hey Alice!");
        messageBuilder.setTimeSent(11111111);

        return messageBuilder.build();
    }
}
