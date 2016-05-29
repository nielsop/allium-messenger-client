package nl.han.asd.project.client.commonclient.heartbeat;

import nl.han.asd.project.client.commonclient.store.Contact;

/**
 * Define the heartbeat scheduling methods.
 *
 * @version 1.0
 */
public interface IHeartbeatService {

    /**
     * Schedule heartbeats for a given contact instance.
     *
     * @param contact to schedule the heartbeats for
     *
     * @throws IllegalArgumentException if contact is null
     */
    public void startHeartbeatFor(Contact contact);

    /**
     * De-schedule the heartbeats for a given contact instance.
     *
     * @param contact to remove the scheduled heartbeats for
     *
     * @return false if no heartbeats were scheduled for this
     *          contact, true if de-scheduling finished successfully
     *
     * @throws InterruptedException if the current thread was
     *          interrupted during the joining of the heartbeat thread
     */
    public boolean stopHeartbeatFor(Contact contact) throws InterruptedException;
}
