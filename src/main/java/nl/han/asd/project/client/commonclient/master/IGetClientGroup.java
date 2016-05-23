package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.master.wrapper.ClientGroupResponseWrapper;

@FunctionalInterface
public interface IGetClientGroup {

    public ClientGroupResponseWrapper getClientGroup();

}
