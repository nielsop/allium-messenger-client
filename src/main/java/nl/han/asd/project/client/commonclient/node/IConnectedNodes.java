package nl.han.asd.project.client.commonclient.node;

import com.google.protobuf.ProtocolStringList;

import java.util.List;

/**
 * Define connectedNodes related methods.
 *
 * @version 1.0
 */
public interface IConnectedNodes {
    /**
     * Set the connectedNodes after successful login.
     *
     * @param connectedNodesList the list of connectedNodes
     */
    void setConnectedNodes(List<String> connectedNodesList);

    /**
     * Unset connectedNodes when after logout.
     */
    void unsetConnectedNodes();
}
