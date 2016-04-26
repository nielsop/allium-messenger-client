package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Created by Julius on 22/04/16.
 */
public class GraphManagerService implements IManageGraph {

    public Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String serverAddress;
    private int serverPort;
    private Graph graph;
    private int versionNumber;

    @Inject
    public GraphManagerService(String serverAddress, int serverPort){
        graph = new Graph();
        final Validation validation = new Validation();
        validation.validateAddress(serverAddress);
        validation.validatePort(serverPort);

        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void checkGraphVersion(int versionNumber) {
        HanRoutingProtocol.GraphUpdateRequest.Builder builder = HanRoutingProtocol.GraphUpdateRequest.newBuilder();
        builder.setCurrentVersion(versionNumber);
        ByteString data = builder.build().toByteString();

        HanRoutingProtocol.EncryptedWrapper.Builder request = HanRoutingProtocol.EncryptedWrapper.newBuilder();
        request.setType(HanRoutingProtocol.EncryptedWrapper.Type.GRAPHUPDATEREQUEST).setData(data);

        final RequestWrapper req = new RequestWrapper(request.build(), socket);
        req.writeToSocket();

        try {
            HanRoutingProtocol.EncryptedWrapper updateResponse;
            while ((updateResponse = HanRoutingProtocol.EncryptedWrapper.parseFrom(socket.getInputStream())) != null) {
                HanRoutingProtocol.GraphUpdateResponse response = HanRoutingProtocol.GraphUpdateResponse.parseFrom(updateResponse.getData());

                for (ByteString bytes : response.getGraphUpdatesList()) {
                    HanRoutingProtocol.GraphUpdate graphUpdate = HanRoutingProtocol.GraphUpdate.parseFrom(bytes);
                    System.out.println(graphUpdate);
                    if(graphUpdate.getNewVersion() == versionNumber){
                        return;
                    }
                    else{
                        versionNumber = graphUpdate.getNewVersion();
                        processGraphUpdates(graphUpdate.getIsFullGraph(),graphUpdate.getAddedNodesList(),graphUpdate.getDeletedNodesList());
                    }
                }

                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void processGraphUpdates(boolean isFullGraph, List<HanRoutingProtocol.Node> addedNodes, List<HanRoutingProtocol.Node> deletedNodes){
        if(isFullGraph){
            graph.resetGraph();
            for (HanRoutingProtocol.Node vertex: addedNodes){
                graph.addNodeVertex(vertex);
            }
            System.out.println(graph);
        }
        else{

        }
    }




}
