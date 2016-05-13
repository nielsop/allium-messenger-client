package nl.han.asd.project.client.commonclient.presentation.gui.model.dashboard;

import nl.han.asd.project.client.commonclient.presentation.gui.controller.DashboardController;
import nl.han.asd.project.client.commonclient.store.Contact;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Kenny
 * @version 1.0
 * @since 12-5-2016
 */
@RunWith(MockitoJUnitRunner.class)
public class ChatModelTest {

    @Mock
    private DashboardController mockDashboardController;
    @Mock
    private Contact currentUser;
    @Mock
    private Contact receiver;

    private ChatModel testChatModel;

    @Before
    public void setUp() throws Exception {
        testChatModel = new ChatModel(mockDashboardController);
        Mockito.when(mockDashboardController.getCurrentUser()).thenReturn(currentUser);
    }

    @Test
    public void setReceiver() throws Exception {
        testChatModel.setSelectedContact(receiver);
        Assert.assertEquals(receiver, testChatModel.getSelectedContact());
    }

    @Ignore
    @Test
    public void testSendMessage() throws Exception {

    }

}
