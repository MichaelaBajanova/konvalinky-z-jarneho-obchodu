package roihunter.task.exceptions;

/**
 * @author Michaela Bajanova (469166)
 */
public class ListNotFoundException extends Exception {

    public ListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListNotFoundException(String message) {
        super(message);
    }
}
