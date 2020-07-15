package exceptions;

public class HalfMapWaterOnSideException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public HalfMapWaterOnSideException(String errorMessage) {
        super("Name: Error HalfMap.", errorMessage);
    }

}
