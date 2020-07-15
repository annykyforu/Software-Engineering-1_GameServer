package exceptions;

public class HalfMapPresentException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public HalfMapPresentException(String errorMessage) {
        super("Name: Duplicate HalfMap.", errorMessage);
    }
}
