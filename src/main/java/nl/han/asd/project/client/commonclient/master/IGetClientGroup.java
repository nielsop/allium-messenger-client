package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

public interface IGetClientGroup {

    List<HanRoutingProtocol.Client> getClientGroup();

}
