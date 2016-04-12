package nl.han.onionmessenger.commonclient.presentation;


import nl.han.onionmessenger.commonclient.message.IMessageBuilder;
import nl.han.onionmessenger.commonclient.store.IContact;

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
