package nl.han.asd.client.commonclient.store;

import com.google.inject.AbstractModule;

/**
 * Created by Marius on 19-04-16.
 */
public class StoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IContact.class).to(ContactStore.class);
        bind(IMessageStore.class).to(MessageStore.class);
        bind(IMessageObserver.class).to(MessageStore.class);
    }
}
