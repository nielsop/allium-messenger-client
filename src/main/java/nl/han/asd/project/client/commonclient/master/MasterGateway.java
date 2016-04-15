package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.registration.IRegistration;
import nl.han.asd.project.client.commonclient.registration.IAuthentication;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MasterGateway implements IGetUpdatedGraph, IGetClients, IHeartbeat, IAuthentication, IRegistration {
    //TODO: missing: IWebService from Master
    public IEncrypt encrypt;

    private String address ;
    private int port;

    /**
     *
     * @param address IPv4 address
     * @param port port to set up for the connection to the master
     */
    public MasterGateway(String address, int port) {
        //Address may not be null
        if(address == null)
            throw new NullPointerException("Invalid adress; adress may not be null.");
        //IP regular expression
        String ipPattern = "^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$";
        //Create pattern object
        int[] addressAsArray = new int[4];
        Pattern r = Pattern.compile(ipPattern);
        //Create matcher object
        Matcher m = r.matcher(address);
        //Check if match is found
        if(m.find()) {
            for(int i = 1 ; i < 5 ; i++){
                //Parse every group to int
                int ipGroup = Integer.parseInt(m.group(i));
                //Check if first value is not 0.
                if(i == 1){
                    if(ipGroup == 0){
                        throw new IllegalArgumentException("First value may not be 0.");
                    }
                }
                //Check if at least one group is greater than 254
                if (ipGroup > 254){
                    throw new IllegalArgumentException("One of the IP-values is greater than 254.");
                }
                //If all values are correct, put the values in an array => [xxx, xxx, xxx, xxx]
                else {
                    addressAsArray[i-1] = ipGroup;
                }
            }
        }
        //No match found
        else{
            throw new IllegalArgumentException("IP format is not valid. Must be xxx.xxx.xxx.xxx");
        }

        this.address = address;
        this.port = port;
    }

    public String registerClient(String data)
    {
        return null;
    }

    public void sendMessage(byte[] data) throws IOException {
        if(data == null)
            throw new NullPointerException("Invalid data; data may not be null.");
        if(data.length < 1)
            throw new IllegalArgumentException("Invalid data; data length expected > 1, found; " + data.length);

        try (Socket s = new Socket(this.address, this.port)) {
            s.getOutputStream().write(data);
            //Flush and close
        }
    }

    public void sendRegistrationMessage(String username, String password) {
        /*//iets doen mt protocol
        //iets doen met excryption -> byte[] erin en byte[] eruit
        //int newByteArrayLength = ... depends on the encrypted string that is received
        byte[] registrationData = new byte[];
        try {
            sendMessage(registrationData);
        }catch(IOException ioException){
            ioException.printStackTrace();
        }*/
    }
}
