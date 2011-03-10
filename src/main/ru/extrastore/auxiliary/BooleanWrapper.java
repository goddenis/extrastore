package ru.extrastore.auxiliary;

/**
* http://extrastore.ru
* Created by Catalyst on 10.03.11 at 18:35
*/
public class BooleanWrapper {  // Enables us to change boolean value from inline class ContextualHttpServletRequest.
    boolean value;

    public BooleanWrapper(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
