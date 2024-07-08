package org.example.quizwebapp.CustomExceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException() {
        super("User already exists");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}