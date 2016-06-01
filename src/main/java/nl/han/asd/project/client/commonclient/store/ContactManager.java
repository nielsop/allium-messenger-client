package nl.han.asd.project.client.commonclient.store;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.master.IGetClientGroup;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ContactManager implements IContactManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactManager.class);

    private static final long MIN_TIMEOUT = 600000;
    private final IGetClientGroup clientGroup;
    private final IContactStore contactStore;
    private long lastGraphUpdate = 0;

    @Inject
    public ContactManager(IGetClientGroup clientGroup, IContactStore contactStore) {
        this.clientGroup = Check.notNull(clientGroup, "clientGroup");
        this.contactStore = Check.notNull(contactStore, "contactStore");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAllContactInformation() {
        if (System.currentTimeMillis() - lastGraphUpdate < MIN_TIMEOUT) {
            return;
        }
        lastGraphUpdate = System.currentTimeMillis();

        HanRoutingProtocol.ClientRequest.Builder builder = HanRoutingProtocol.ClientRequest.newBuilder();
        builder.setClientGroup(1);
        try {
            HanRoutingProtocol.ClientResponse clientWrapper = clientGroup.getClientGroup(builder.build());
            System.out.println("Clients found:" + clientWrapper.getClientsCount());
            for (HanRoutingProtocol.Client client : clientWrapper.getClientsList()) {
                System.out.println("Updated client: " + client.getUsername());
                List<String> connectNodes = client.getConnectedNodesList();
                System.out.println(connectNodes);
                contactStore.updateUserInformation(client.getUsername(), client.getPublicKey().toByteArray(), true, connectNodes);
            }
        } catch (IOException | MessageNotSentException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
