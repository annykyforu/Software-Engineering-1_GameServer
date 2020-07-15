package exceptions;

public class HalfMapWaterException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public HalfMapWaterException(String errorMessage) {
        super("Name: Error HalfMap.", errorMessage);
    }

}
