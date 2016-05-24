package nl.han.asd.project.client.commonclient.connection;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.GeneratedMessage;

import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper.Type;

/**
 * Define available read and write operations.
 *
 * @version 1.0
 */
public interface IConnectionService {

    /**
     * Wrap the provided message in a new Wrapper instance encrypting
     * the message provided the public key of the receiver was defined.
     *
     * @param message to-be-wrapped message
     * @param type type referring to this message
     *
     * @return Wrapper containing the message and the provided type
     *
     * @throws IllegalArgumentException if message and/or type is null
     */
    public <T extends GeneratedMessage> Wrapper wrap(T message, Type type);

    /**
     * Transmit the provided message to the host.
     *
     * <p>
     * This method should only be used for time-critical messages
     * that which transmission is unable to wait.
     *
     * <p>
     * Note that the delay before sending the message is at most
     * equal to timeout excluding the time required to setup a new
     * connection.
     *
     * @param T type of wrapper
     *
     * @param wrapper to-be-send wrapper
     * @param timeout maximum timout to wait before giving up
     *          and creating a new socket
     * @param unit the time unit of the timout parameter
     *
     * @throws IOException if the function was unable to send
     *          the wrapper due to a socket related
     *          exception
     * @throws MessageNotSendException if the connection service
     *          was unable to send the message. Note that
     *          this exception is not thrown on Socket related
     *          exceptions. See IOException.
     */
    public <T extends GeneratedMessage> void write(T wrapper, long timeout, TimeUnit unit)
            throws IOException, MessageNotSendException;

    /**
     * Transmit the provided message to the host.
     *
     * <p>
     * Note that this method may block for a long time
     * depending on the number of other threads waiting
     * to send a message.
     *
     * @param T type of wrapper
     *
     * @param wrapper to-be-send wrapper
     *
     * @throws IOException if the function was unable to send
     *          the wrapper due to a socket related
     *          exception
     * @throws MessageNotSendException if the connection service
     *          was unable to send the message. Note that
     *          this exception is not thrown on Socket related
     *          exceptions. See IOException.
     */
    public <T extends GeneratedMessage> void write(T wrapper) throws IOException, MessageNotSendException;

    /**
     * Transmit the provided message to the host and wait for the
     * host to send a response.
     *
     * <p>
     * This method should only be used for time-critical messages
     * that which transmission is unable to wait.
     *
     * <p>
     * Note that the delay before sending the message is at most
     * equal to timeout excluding the time required to setup a new
     * connection.
     *
     * @param T type of wrapper
     *
     * @param wrapper to-be-send wrapper
     * @param timeout maximum timeout to wait before giving up
     *          and creating a new socket
     * @param unit the time unit of the timeout parameter
     *
     * @throws IOException if the function was unable to send
     *          the wrapper due to a socket related
     *          exception
     * @throws MessageNotSendException if the connection service
     *          was unable to send the message. Note that
     *          this exception is not thrown on Socket related
     *          exceptions. See IOException.
     */
    public <T extends GeneratedMessage> GeneratedMessage writeAndRead(T wrapper, long timeout, TimeUnit unit)
            throws IOException, MessageNotSendException;

    /**
     * Transmit the provided message to the host and wait for the
     * host to send a response.
     *
     * <p>
     * Note that this method may block for a long time
     * depending on the number of other threads waiting
     * to send a message.
     *
     * @param T type of wrapper
     *
     * @param wrapper to-be-send wrapper
     *
     * @throws IOException if the function was unable to send
     *          the wrapper due to a socket related
     *          exception
     * @throws MessageNotSendException if the connection service
     *          was unable to send the message. Note that
     *          this exception is not thrown on Socket related
     *          exceptions. See IOException.
     */
    public <T extends GeneratedMessage> GeneratedMessage writeAndRead(T wrapper)
            throws IOException, MessageNotSendException;
}
