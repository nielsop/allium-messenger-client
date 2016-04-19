package nl.han.asd.client.commonclient.presentation;


import nl.han.asd.client.commonclient.message.IMessageBuilder;
import nl.han.asd.client.commonclient.store.IContact;

/**
 * Android/Desktop application
 * <p>
 * Leave empty until we know what to do with it
 */
public class PresentationLayer implements IMessageObserver {
    //TODO: android app? desktop app?
    public IContact contact;
    public IMessageBuilder builder;
}
