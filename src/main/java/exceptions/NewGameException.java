package exceptions;

public class NewGameException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public NewGameException(String errorMessage) {
        super("Name: Error creating new Game.", errorMessage);
    }
}