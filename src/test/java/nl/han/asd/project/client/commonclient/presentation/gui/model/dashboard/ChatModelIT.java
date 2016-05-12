package nl.han.asd.project.client.commonclient.presentation.gui.model.dashboard;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.DashboardController;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

/**
 *
 * @author Kenny
 * @version 1.0
 * @since 12-5-2016
 */
public class ChatModelIT {

    private static GUI testGui = new GUI();
    @Mock
    private Message mockMessage;
    private ChatModel testChatModel;
    private DashboardController testDashboardController;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        testGui = new GUI();
        Injector injector = Guice.createInjector(new CommonclientModule());
        injector.injectMembers(testGui);
    }

    @Before
    public void setUp() throws Exception {
        testDashboardController = new DashboardController(testGui);
        testChatModel = new ChatModel(testDashboardController);
    }

    @Ignore("Can't send message yet.")
    @Test
    public void testSendMessage() throws Exception {
        testChatModel.sendMessage(mockMessage);
    }
}
