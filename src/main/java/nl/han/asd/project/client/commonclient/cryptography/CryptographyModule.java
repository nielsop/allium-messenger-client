package nl.han.asd.project.client.commonclient.cryptography;

import com.google.inject.AbstractModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;

/**
 * @author Marius
 * @version 1.0
 * @since 27-05-16
 */
public class CryptographyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IEncryptionService.class).to(EncryptionService.class);
    }
}