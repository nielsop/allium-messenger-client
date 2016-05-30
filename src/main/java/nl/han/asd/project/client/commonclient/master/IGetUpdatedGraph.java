package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.master.wrapper.UpdatedGraphResponseWrapper;

public interface IGetUpdatedGraph {
    UpdatedGraphResponseWrapper IGetUpdatedGraph(int currentVersion);
}
