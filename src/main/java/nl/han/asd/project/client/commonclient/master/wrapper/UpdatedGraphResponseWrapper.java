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
 * Stores the updated graph.
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 25-4-2016
 */
public class UpdatedGraphResponseWrapper {

    /**
     * Contains all the updated graphs, differentiated by version.
     */
    public List<UpdatedGraphWrapper> updatedGraphs = new ArrayList<>();

    /**
     * Creates a new updated graph response wrapper from a list of ByteStrings containing the individual graph updates.
     *
     * @param graphUpdates The graph updates
     */
    public UpdatedGraphResponseWrapper(List<ByteString> graphUpdates) {
        graphUpdates.forEach(graphUpdate -> {
            try {
                updatedGraphs
                        .add(new UpdatedGraphWrapper(readGeneric(HanRoutingProtocol.GraphUpdate.class, graphUpdate)));
            } catch (SocketException | InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Returns the last (i.e. most recent) graph.
     *
     * @return The last (i.e. most recent) graph.
     */
    public UpdatedGraphWrapper getLast() {
        return updatedGraphs.get(updatedGraphs.size() - 1);
    }

    /**
     * Parses the GeneratedMessage to a HanRoutingProtocol response.
     *
     * @param classDescriptor The HanRoutingProtocol class descriptor to parse the generated message to.
     * @param b               The message byte array in the ByteString format.
     * @param <T>             The type of the class to parse to
     * @return A parsed GeneratedMessage with a class descriptor of <tt>classDescriptor</tt>
     * @throws SocketException                Throws a SocketException incase the Socket connection can not be opened.
     * @throws InvalidProtocolBufferException Throws a InvalidProtocolBufferException incase the wrong protocol buffer
     *                                        is being used to parse the message.
     */
    private <T extends GeneratedMessage> T readGeneric(Class<T> classDescriptor, ByteString b)
            throws SocketException, InvalidProtocolBufferException {
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

    /**
     * Wrapper class for individual updated graphs.
     */
    public class UpdatedGraphWrapper {
        /**
         * Stores the new version.
         */
        public int newVersion;

        /**
         * Stores whether or not the graph is a full graph.
         */
        public boolean isFullGraph;

        /**
         * Contains a list of all nodes that were added to the graph this version.
         */
        public List<HanRoutingProtocol.Node> addedNodes;

        /**
         * Contains a list of all nodes taht were deleted from the graph this version.
         */
        public List<HanRoutingProtocol.Node> deletedNodes;

        /**
         * Creates a new UpdatedGraphWrapper.
         *
         * @param graphUpdate The HRP updated graph.
         */
        public UpdatedGraphWrapper(HanRoutingProtocol.GraphUpdate graphUpdate) {
            this.newVersion = graphUpdate.getNewVersion();
            this.isFullGraph = graphUpdate.getIsFullGraph();
            this.addedNodes = graphUpdate.getAddedNodesList();
            this.deletedNodes = graphUpdate.getDeletedNodesList();
        }
    }

}