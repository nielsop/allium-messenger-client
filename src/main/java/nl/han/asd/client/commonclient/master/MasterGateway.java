package nl.han.asd.client.commonclient.master;

import nl.han.asd.client.commonclient.cryptography.IEncrypt;

public class MasterGateway implements IGetUpdatedGraph, IGetClients, IHeartbeat {
    //TODO: missing: IWebService from Master
    public IEncrypt encrypt;

}
