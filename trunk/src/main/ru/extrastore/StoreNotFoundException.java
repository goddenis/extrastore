package ru.extrastore;

/**
 * http://extrastore.ru
 * Created by Catalyst on 08.03.11 at 14:32
 */
public class StoreNotFoundException extends Exception {
    public StoreNotFoundException(String message) {
        super(message);
    }

    public StoreNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
