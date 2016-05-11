package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Jevgeni on 13-4-2016.
 */
public class ReceiveMessageTest {
    public final MessageProcessingService mpService = new MessageProcessingService(null);

    @Test
    public void ProcessReceivedMessage() {
        HanRoutingProtocol.Message.Builder builder = HanRoutingProtocol.Message.newBuilder();
        builder.setId("1000");
        builder.setSender("Contact");
        builder.setText("Test message");

        //mpService.processMessage(builder.build());

        //        mpService.messageStore.findMessage(builder.build());

        //EncryptedMessage encryptedMessage = mpService.peelMessagePacket(null);
        // Assert.assertEquals(encryptedMessage.getUsername(), "ikzelf");
        Assert.assertEquals(1, 1); //TODO: Testcase afmaken!
    }
}
