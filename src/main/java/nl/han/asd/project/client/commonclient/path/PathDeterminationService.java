package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.master.IGetClients;
import nl.han.asd.project.client.commonclient.master.IGetUpdatedGraph;

public class PathDeterminationService implements IGetPath {
    public IGetUpdatedGraph updatedGraph;
    public IGetClients clients;
}
