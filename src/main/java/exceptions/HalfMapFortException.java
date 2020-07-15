package exceptions;

public class HalfMapFortException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public HalfMapFortException(String errorMessage) {
        super("Name: Error HalfMap.", errorMessage);
    }

}
