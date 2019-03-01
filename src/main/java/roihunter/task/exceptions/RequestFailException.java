package roihunter.task.exceptions;

/**
 * @author Michaela Bajanova (469166)
 */
public class RequestFailException extends Exception {

    public RequestFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestFailException(String message) {
        super(message);
    }
}
