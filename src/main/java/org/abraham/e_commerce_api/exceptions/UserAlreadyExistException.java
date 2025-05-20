package org.abraham.e_commerce_api.exceptions;


public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("User already exists with this email address");
    }
}
