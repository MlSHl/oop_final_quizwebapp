package org.example.quizwebapp.CustomExceptions;

public class RequestDoesntExist extends Exception{

        public RequestDoesntExist() {
            super("Friend requset doesn't exist");
        }

        public RequestDoesntExist(String message) {
            super(message);
        }

        public RequestDoesntExist(String message, Throwable cause) {
            super(message, cause);
        }

        public RequestDoesntExist(Throwable cause) {
            super(cause);
        }
}
