package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by BILLPOORTS on 22-4-2016.
 */
public class PackerTest {

    private Packer packer = null;

    @Before
    public void InitPacker() {
        final Injector injector = Guice.createInjector(new EncryptionModule());
        packer = new Packer(new CryptographyService(injector.getInstance(IEncryptionService.class)));
    }

    @Test
    public void TestPacking() throws InvalidProtocolBufferException {
        HanRoutingProtocol.EncryptedWrapper packedData = pack();
        UnpackedMessage unpackedData = unpack(packedData);
        HanRoutingProtocol.ClientLoginRequest wrapper = HanRoutingProtocol.ClientLoginRequest.parseFrom(unpackedData.getData());
    }

    public HanRoutingProtocol.EncryptedWrapper pack() {
        // Simulate a builder..
        HanRoutingProtocol.ClientLoginRequest.Builder builder = HanRoutingProtocol.ClientLoginRequest.newBuilder();
        builder.setPublicKey("test");
        builder.setUsername("test");
        builder.setPassword("test");

        // Pack..
        HanRoutingProtocol.EncryptedWrapper packed = packer.pack(builder, packer.getMyPublicKey());

        return packed;
    }

    public UnpackedMessage unpack(HanRoutingProtocol.EncryptedWrapper packed)
    {
        UnpackedMessage unpacked = packer.unpack(packed);

        return unpacked;
    }


    @Test
    public void TestUnpacking() throws InvalidProtocolBufferException {
        HanRoutingProtocol.EncryptedWrapper packedData = pack();
        UnpackedMessage unpackedMessage = unpack(packedData);

        HanRoutingProtocol.ClientLoginRequest.parseFrom(unpackedMessage.getData());

        assertEquals(unpackedMessage.getDataType(), HanRoutingProtocol.EncryptedWrapper.Type.CLIENTLOGINREQUEST);
        assertEquals(unpackedMessage.getDataMessage().getClass(), HanRoutingProtocol.ClientLoginRequest.class);

        HanRoutingProtocol.ClientLoginRequest clr = (HanRoutingProtocol.ClientLoginRequest)unpackedMessage.getDataMessage().getParserForType().parseFrom(unpackedMessage.getData());
    }

}