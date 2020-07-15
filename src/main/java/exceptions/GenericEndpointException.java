package exceptions;

public class GenericEndpointException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String errorName;

    public GenericEndpointException(String errorName, String errorMessage) {
        super(errorMessage);
        this.errorName = errorName;
    }

    public String getErrorName() {
        return errorName;
    }
}