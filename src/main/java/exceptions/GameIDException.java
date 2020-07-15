package exceptions;

public class GameIDException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public GameIDException(String errorMessage) {
        super("Name: Error GameID.", errorMessage);
    }
}