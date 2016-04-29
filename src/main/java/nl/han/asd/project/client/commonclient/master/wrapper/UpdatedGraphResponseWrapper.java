package nl.han.asd.project.client.commonclient.master.wrapper;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.lang.reflect.Field;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 25-4-2016
 */
public class UpdatedGraphResponseWrapper {

    public List<UpdatedGraphWrapper> updatedGraphs = new ArrayList<>();

    public UpdatedGraphResponseWrapper(List<ByteString> graphUpdates) {
        graphUpdates.forEach(graphUpdate -> {
            try {
                updatedGraphs.add(new UpdatedGraphWrapper(readGeneric(HanRoutingProtocol.GraphUpdate.class, graphUpdate)));
            } catch (SocketException | InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        });
    }

    public UpdatedGraphWrapper getLast() {
        return updatedGraphs.get(updatedGraphs.size() - 1);
    }

    private <T extends GeneratedMessage> T readGeneric(Class<T> classDescriptor, ByteString b) throws SocketException, InvalidProtocolBufferException {
        byte[] buffer = b.toByteArray();
        if (buffer != null) {
            try {
                Field defaultInstanceField = classDescriptor.getDeclaredField("DEFAULT_INSTANCE");
                defaultInstanceField.setAccessible(true);
                T defaultInstance = (T) defaultInstanceField.get(null);
                return (T) defaultInstance.getParserForType().parseFrom(buffer);
            } catch (IllegalAccessException | InvalidProtocolBufferException e) {
                // return null
            } catch (NoSuchFieldException e) {
                throw new InvalidProtocolBufferException("Invalid class provided.");
            }
        }
        return null;
    }

    public class UpdatedGraphWrapper {
        public int newVersion;
        public boolean isFullGraph;
        public List<HanRoutingProtocol.Node> addedNodes;
        public List<HanRoutingProtocol.Node> deletedNodes;

        public UpdatedGraphWrapper(HanRoutingProtocol.GraphUpdate graphUpdate) {
            this.newVersion = graphUpdate.getNewVersion();
            this.isFullGraph = graphUpdate.getIsFullGraph();
            this.addedNodes = graphUpdate.getAddedNodesList();
            this.deletedNodes = graphUpdate.getDeletedNodesList();
        }
    }

}
