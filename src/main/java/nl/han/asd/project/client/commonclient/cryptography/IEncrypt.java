package nl.han.asd.project.client.commonclient.cryptography;

/**
 * Created by Niels Bokmans on 12-4-2016.
 */
public interface IEncrypt {
    String encryptData(String data,String publicKey);
}
