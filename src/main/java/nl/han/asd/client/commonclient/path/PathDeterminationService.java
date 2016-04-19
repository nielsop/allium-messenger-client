package nl.han.asd.client.commonclient.path;

import nl.han.asd.client.commonclient.master.IGetUpdatedGraph;
import nl.han.asd.client.commonclient.master.IGetClients;

public class PathDeterminationService implements IGetPath {
    public IGetUpdatedGraph updatedGraph;
    public IGetClients clients;
}
