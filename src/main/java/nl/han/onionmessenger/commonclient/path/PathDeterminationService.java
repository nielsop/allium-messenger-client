package nl.han.onionmessenger.commonclient.path;

import nl.han.onionmessenger.commonclient.master.IGetClients;
import nl.han.onionmessenger.commonclient.master.IGetUpdatedGraph;

public class PathDeterminationService implements IGetPath {
    public IGetUpdatedGraph updatedGraph;
    public IGetClients clients;
}
