package nl.han.asd.client.commonclient.path;

import nl.han.asd.client.commonclient.master.IGetClients;
import nl.han.asd.client.commonclient.master.IGetUpdatedGraph;

public class PathDeterminationService implements IGetPath {
    public IGetUpdatedGraph updatedGraph;
    public IGetClients clients;
}
