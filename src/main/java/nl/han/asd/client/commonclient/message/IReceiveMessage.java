package nl.han.asd.client.commonclient.message;

public interface IReceiveMessage {
    public EncryptedMessage peelMessagePacket(Object messagePacket);
}
