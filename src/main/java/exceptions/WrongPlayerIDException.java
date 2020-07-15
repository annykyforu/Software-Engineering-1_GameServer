package exceptions;

public class WrongPlayerIDException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public WrongPlayerIDException(String errorMessage) {
        super("Name: Error playerID.", errorMessage);
    }
}
