package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.lang.reflect.Field;
import java.rmi.activation.UnknownObjectException;
import java.util.List;

/**
 * Wrapper that wraps EncryptedWrapper.
 */
public class Packer {
    private CryptographyService cryptographyService = null;

    public Packer(final CryptographyService cryptographyService)
    {
        this.cryptographyService = cryptographyService;
    }

    /**
     * Packs a builder inside an EncrytpedWrapper message.
     * @param originalBuilder Any Builder from the protocol buffer class.
     * @param publicKey The public key that should be included inside the EncryptedWrapper message.
     * @return The byte array that represents the EncryptedWrapper.
     */
    public byte[] pack(final GeneratedMessage.Builder originalBuilder, final String publicKey) {
        HanRoutingProtocol.EncryptedWrapper.Builder builder = HanRoutingProtocol.EncryptedWrapper.newBuilder();

        HanRoutingProtocol.EncryptedWrapper.Type type = protocolMessageDescriptorToWrapperType(originalBuilder.getDescriptorForType());
        builder.setType(type);
        builder.setPublicKey(publicKey);
        builder.setData(originalBuilder.build().toByteString());

        byte[] buffer = builder.build().toByteArray();

        buffer = cryptographyService.encryptData(ByteString.copyFrom(buffer), publicKey).toByteArray();
        return buffer;
    }

    /**
     * Unpacks a byte array that was read from the input stream.
     * @param packed byte array that represents an EncryptedWrapper.
     * @return The unpacked version of the encrypted wrapper.
     */
    public ParsedMessage unpack(final byte[] packed) {
        HanRoutingProtocol.EncryptedWrapper encryptedWrapper;
        try {
            byte[] buffer = cryptographyService.decryptData(ByteString.copyFrom(packed)).toByteArray();
            encryptedWrapper = HanRoutingProtocol.EncryptedWrapper.parseFrom(buffer);
        } catch (InvalidProtocolBufferException e) {
            return null;
        }

        //String publicKey = encryptedWrapper.getPublicKey();
        HanRoutingProtocol.EncryptedWrapper.Type type = encryptedWrapper.getType();
        byte[] wrappedData = encryptedWrapper.getData().toByteArray();

        ParsedMessage parsedMessage;
        try {
            GeneratedMessage message = wrapperTypeToProtocolMessage(type);
            parsedMessage = new ParsedMessage(wrappedData, type, message);
        } catch (UnknownObjectException e) {
            return null;
        }

        return parsedMessage;
    }

    /**
     * Converts an EncryptedWrapper.Type to its matching protocol buffer class.
     * @param type The EncryptedWrapper.Type that needs to be converted.
     * @param <T> Any class, derived from GeneratedMessage.
     * @return An class that extends from GeneratedMessage which can be used to decode the data inside an EncryptedWrapper.
     * @throws UnknownObjectException
     */
    private <T extends GeneratedMessage> T wrapperTypeToProtocolMessage(final HanRoutingProtocol.EncryptedWrapper.Type type) throws UnknownObjectException {
        String name = type.name();

        List<Descriptors.Descriptor> descriptorList = HanRoutingProtocol.getDescriptor().getMessageTypes();
        for(Descriptors.Descriptor descriptor : descriptorList)
        {
            if (descriptor.getName().toUpperCase().equals((name)))
            {
                try {
                    //nl.han.asd.project.protocol.HanRoutingProtocol$ClientLoginRequest
                    String internalName = String.format("%s$%s", HanRoutingProtocol.class.getCanonicalName(), descriptor.getName());
                    Field defaultInstanceField = Class.forName(internalName).getDeclaredField("DEFAULT_INSTANCE");
                    defaultInstanceField.setAccessible(true);
                    return (T) defaultInstanceField.get(null);
                } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Converts a descriptor taken from the Builder class to convert it to a valid EncryptedWrapper.Type.
     * @param classDescriptor The descriptor type of a builder.
     * @return The EncryptedWrapper.Type that is equivalent to the descriptor type.
     */
    private HanRoutingProtocol.EncryptedWrapper.Type protocolMessageDescriptorToWrapperType(final Descriptors.Descriptor classDescriptor)
    {
        String name = classDescriptor.getFullName();
        String capitalizedCleanName = name.toUpperCase();
        return HanRoutingProtocol.EncryptedWrapper.Type.valueOf(capitalizedCleanName);
    }


}
