package exceptions;

public class HalfMapIslandException extends GenericEndpointException {
	private static final long serialVersionUID = 1L;

    public HalfMapIslandException(String errorMessage) {
        super("Name: Error HalfMap.", errorMessage);
    }
}
