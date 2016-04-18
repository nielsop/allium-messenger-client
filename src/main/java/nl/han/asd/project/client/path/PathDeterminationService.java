package nl.han.asd.project.client.path;

import nl.han.asd.project.client.master.IGetClients;
import nl.han.asd.project.client.master.IGetUpdatedGraph;

public class PathDeterminationService implements IGetPath {
    public IGetUpdatedGraph updatedGraph;
    public IGetClients clients;
}
