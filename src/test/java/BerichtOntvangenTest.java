import nl.han.asd.client.commonclient.message.EncryptedMessage;
import nl.han.asd.client.commonclient.message.MessageProcessingService;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by BILLPOORTS on 13-4-2016.
 */
public class BerichtOntvangenTest {
    public final MessageProcessingService mpService = new MessageProcessingService();

    @Test
    public void ProcessReceivedMessage()
    {
        EncryptedMessage encryptedMessage = mpService.peelMessagePacket(null);
        Assert.assertEquals(encryptedMessage.getUsername(), "ikzelf");
    }
}
