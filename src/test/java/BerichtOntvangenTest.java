import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.message.EncryptedMessage;
import nl.han.asd.project.client.commonclient.message.MessageProcessingService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Test;

/**
 * Created by BILLPOORTS on 13-4-2016.
 */
public class BerichtOntvangenTest {
    public final MessageProcessingService mpService = new MessageProcessingService(null);

    @Test
    public void ProcessReceivedMessage()
    {
        HanRoutingProtocol.EncryptedMessage.Builder builder = HanRoutingProtocol.EncryptedMessage.newBuilder();
        builder.setUsername("User");
        builder.setEncryptedData(ByteString.copyFrom(new byte[] { 0x00, 0x01, 0x02 }, 0, 2));
        builder.setIPaddress("127.0.0.1");
        builder.setPort(1000);

        mpService.receiveMessage(EncryptedMessage.parseFrom(builder.build()));
        //Assert.assertEquals(encryptedMessage.getUsername(), "ikzelf");
    }
}
