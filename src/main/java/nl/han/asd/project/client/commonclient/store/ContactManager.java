package nl.han.asd.project.client.commonclient.store;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.master.IGetClientGroup;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;

/**
 * Created by Raoul on 1/6/2016.
 */
public class ContactManager implements IContactManager {

    private final IGetClientGroup clientGroup;
    private final IContactStore contactStore;

    @Inject
    public ContactManager(IGetClientGroup clientGroup, IContactStore contactStore) {
        this.clientGroup = clientGroup;
        this.contactStore = contactStore;
    }

    @Override
    public void updateConnectedNodes() {
        HanRoutingProtocol.ClientRequest.Builder builder = HanRoutingProtocol.ClientRequest.newBuilder();
        builder.setClientGroup(0);
        try {
            HanRoutingProtocol.Client clients = clientGroup.getClientGroup(builder.build());
        } catch (IOException | MessageNotSentException e) {
            e.printStackTrace();
        }
    }
}
