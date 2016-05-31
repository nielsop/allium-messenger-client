package nl.han.asd.project.client.commonclient.store;

import com.google.inject.Guice;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.client.commonclient.persistence.PersistenceModule;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Jevgeni on 12-5-2016.
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore
public class MessageStoreTest {

    @Mock
    private MessageStore messageStore;

}
