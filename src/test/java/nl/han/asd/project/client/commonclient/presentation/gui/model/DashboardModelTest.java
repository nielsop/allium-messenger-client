package nl.han.asd.project.client.commonclient.presentation.gui.model;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.presentation.PresentationLayer;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.store.Contact;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.times;

/**
 *
 * @author Kenny
 * @version 1.0
 * @since 12-5-2016
 */
@RunWith(MockitoJUnitRunner.class)
public class DashboardModelTest {

    private static GUI testGui;
    @Mock
    private Contact mockContact;
    @Mock
    private PresentationLayer mockPresentationLayer;
    @Mock
    private Message mockMessage;

    private DashboardModel testDashboardModel;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        testGui = new GUI();
        Injector injector = Guice.createInjector(new CommonclientModule());
        injector.injectMembers(testGui);
    }

    @Before
    public void setUp() throws Exception {

        //when(testGui.getPresentationLayer()).thenReturn(mockPresentationLayer);
        //when(mockPresentationLayer.getCurrentUser()).thenReturn(mockContact);

        testDashboardModel = new DashboardModel(testGui);
    }

    @Test
    public void testGetMessagesFromContact() throws Exception {
        Assert.assertThat(testDashboardModel.getMessages(mockContact), instanceOf(List.class));
    }

    @Ignore("Can't send messages yet.")
    @Test //Test is worth nothing, could be implemented in DashboardModelIT once sendMessage is available.
    public void testSendMessageExecutesSendMessageOnPresentationLayer() throws Exception {
        testDashboardModel.getGUI().getPresentationLayer().sendMessage(mockMessage);
        Mockito.verify(testDashboardModel.getGUI().getPresentationLayer(), times(1)).sendMessage(mockMessage);
    }

    @Test
    public void testGetContactsReturnsListObject() throws Exception {
        Assert.assertThat(testDashboardModel.getContacts(), instanceOf(List.class));
    }
}
