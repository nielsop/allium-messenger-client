package nl.han.asd.project.client.commonclient.master.wrapper;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 22-4-2016
 */
public class LoginResponseWrapper {
    public List<HanRoutingProtocol.Node> nodeList;
    public String secretHash;
    public HanRoutingProtocol.ClientLoginResponse.Status status;

    public LoginResponseWrapper(List<HanRoutingProtocol.Node> nodeList, String secretHash,
                                HanRoutingProtocol.ClientLoginResponse.Status status) {
        this.nodeList = nodeList;
        this.secretHash = secretHash;
        this.status = status;
    }
}
