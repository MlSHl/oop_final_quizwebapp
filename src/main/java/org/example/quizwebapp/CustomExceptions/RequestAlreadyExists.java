package org.example.quizwebapp.CustomExceptions;

public class RequestAlreadyExists extends Exception {
    public RequestAlreadyExists() {
        super("Friend requset already exists");
    }

    public RequestAlreadyExists(String message) {
        super(message);
    }

    public RequestAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestAlreadyExists(Throwable cause) {
        super(cause);
    }
}
