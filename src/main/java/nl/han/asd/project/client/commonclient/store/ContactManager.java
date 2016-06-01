package nl.han.asd.project.client.commonclient.store;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.master.IGetClientGroup;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.util.List;

/**
 * Created by Raoul on 1/6/2016.
 */
public class ContactManager implements IContactManager {

    private static final long MIN_TIMEOUT = 600000;
    private final IGetClientGroup clientGroup;
    private final IContactStore contactStore;
    private long lastGraphUpdate = 0;

    @Inject
    public ContactManager(IGetClientGroup clientGroup, IContactStore contactStore) {
        this.clientGroup = clientGroup;
        this.contactStore = contactStore;
    }

    @Override
    public void updateAllContactInformation() {
        if (System.currentTimeMillis() - lastGraphUpdate < MIN_TIMEOUT) {
            return;
        }
        lastGraphUpdate = System.currentTimeMillis();

        HanRoutingProtocol.ClientRequest.Builder builder = HanRoutingProtocol.ClientRequest.newBuilder();
        builder.setClientGroup(0);
        try {
            HanRoutingProtocol.ClientResponse clientWrapper = clientGroup.getClientGroup(builder.build());
            for (HanRoutingProtocol.Client client : clientWrapper.getClientsList()) {
                List<String> connectNodes = client.getConnectedNodesList();
                contactStore.updateUserInformation(client.getUsername(), client.getPublicKey().toByteArray(), true, connectNodes);
            }
        } catch (IOException | MessageNotSentException e) {
            e.printStackTrace();
        }
    }
}
