package exceptions;

public class NoPlayerException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public NoPlayerException(String errorMessage) {
        super("Name: Error playerID.", errorMessage);
    }
}
