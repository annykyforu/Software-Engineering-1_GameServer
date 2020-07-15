package exceptions;

public class PlayerMoveException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public PlayerMoveException(String errorMessage) {
        super("Name: Error Action.", errorMessage);
    }
}
