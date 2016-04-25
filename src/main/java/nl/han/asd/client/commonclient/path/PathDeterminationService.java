package nl.han.asd.client.commonclient.path;

import nl.han.asd.client.commonclient.master.IGetClientGroup;
import nl.han.asd.client.commonclient.master.IGetUpdatedGraph;

import javax.inject.Inject;

public class PathDeterminationService implements IGetPath {
    public IGetUpdatedGraph updatedGraph;
    public IGetClientGroup clientGroup;

    @Inject
    public PathDeterminationService(IGetUpdatedGraph updatedGraph, IGetClientGroup clientGroup) {
        this.updatedGraph = updatedGraph;
        this.clientGroup = clientGroup;
    }
}
