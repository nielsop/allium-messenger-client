package nl.han.asd.project.client.commonclient.connection;

/**
 * RuntimeException thrown by the Packer class.
 *
 * @author Jevgeni Geurtsen
 */
class PackerException extends RuntimeException {

    public PackerException(Exception exception) {
        super(exception);
    }

    public PackerException(String message){
        super(message);
    }
}
