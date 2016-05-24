package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.master.wrapper.UpdatedGraphResponseWrapper;

@FunctionalInterface
public interface IGetUpdatedGraph {

    UpdatedGraphResponseWrapper getUpdatedGraph(int currentVersion);

}
