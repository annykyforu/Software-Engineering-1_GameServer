package exceptions;

public class WaitFor2PlayerException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public WaitFor2PlayerException(String errorMessage) {
        super("Name: Error Players.", errorMessage);
    }
}
