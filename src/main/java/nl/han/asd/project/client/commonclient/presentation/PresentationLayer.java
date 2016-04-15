package nl.han.asd.project.client.commonclient.presentation;

import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.store.IContact;

/**
 * Android/Desktop application
 * <p>
 * Leave empty until we know what to do with it
 */
public class PresentationLayer implements IMessageObserver {
    //TODO: android app? desktop app?
    public IContact contact;
    public IMessageBuilder builder;
    private MasterGateway gateway;

    public PresentationLayer(MasterGateway gateway){
        this.gateway = gateway;
    }

    public void register(){
        gateway.sendRegistrationMessage("username", "password");
    }
}
