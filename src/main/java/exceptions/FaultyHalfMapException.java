package exceptions;

public class FaultyHalfMapException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public FaultyHalfMapException(String errorMessage) {
        super("Name: Error HalfMap.", errorMessage);
    }
}
