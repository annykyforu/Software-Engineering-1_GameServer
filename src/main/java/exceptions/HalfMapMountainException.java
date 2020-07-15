package exceptions;

public class HalfMapMountainException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public HalfMapMountainException(String errorMessage) {
        super("Name: Error HalfMap.", errorMessage);
    }

}
