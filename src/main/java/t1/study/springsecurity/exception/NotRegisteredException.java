package t1.study.springsecurity.exception;

public class NotRegisteredException extends RuntimeException {

    public NotRegisteredException(String message) {
        super(message);
    }
}
