package nl.han.asd.project.client.commonclient.store;

import com.google.inject.Guice;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.client.commonclient.persistence.PersistenceModule;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Before;
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
public class MessageStoreTest {

    @Mock
    private MessageStore messageStore;

    @Test
    public void testMessageAdded() {
        HanRoutingProtocol.Message message = getMessage();

        when(messageStore.findMessageByID(message.getId())).thenReturn(message);

        messageStore.addMessage(message, "recipient");
        HanRoutingProtocol.Message result = messageStore.findMessageByID(message.getId());

        assertEquals(result, message);
    }

    private HanRoutingProtocol.Message getMessage() {
        HanRoutingProtocol.Message.Builder messageBuilder = HanRoutingProtocol.Message.newBuilder();
        messageBuilder.setId("11111111");
        messageBuilder.setSender("Alice");
        messageBuilder.setText("Test");
        messageBuilder.setTimeSent(22222222);

        return messageBuilder.build();
    }
}
