package exceptions;

public class HalfMapGrassException extends GenericEndpointException {
    private static final long serialVersionUID = 1L;

    public HalfMapGrassException(String errorMessage) {
        super("Name: Error HalfMap.", errorMessage);
    }
}
