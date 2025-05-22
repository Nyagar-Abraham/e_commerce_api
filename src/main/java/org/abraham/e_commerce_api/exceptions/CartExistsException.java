package org.abraham.e_commerce_api.exceptions;

public class CartExistsException extends RuntimeException {
    public CartExistsException(String message) {
        super(message);
    }
}
