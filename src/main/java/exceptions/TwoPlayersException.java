package exceptions;

public class TwoPlayersException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public TwoPlayersException(String errorMessage) {
        super("Name: Error Player Registration.", errorMessage);
    }
}