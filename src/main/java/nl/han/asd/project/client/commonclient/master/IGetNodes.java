package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

/**
 * Created by Jevgeni on 19-5-2016.
 */
public interface IGetNodes {
    List<HanRoutingProtocol.Node> getAllNodes();
}
