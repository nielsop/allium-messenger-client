package nl.han.onionmessenger.commonclient.master;

import nl.han.onionmessenger.commonclient.cryptography.IEncrypt;

public class MasterGateway implements IGetUpdatedGraph, IGetClients, IHeartbeat {
    //TODO: missing: IWebService from Master
    public IEncrypt encrypt;

}
