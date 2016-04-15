package unit.commonclient.path;

import unit.commonclient.master.IGetClients;
import unit.commonclient.master.IGetUpdatedGraph;

public class PathDeterminationService implements IGetPath {
    public IGetUpdatedGraph updatedGraph;
    public IGetClients clients;
}
