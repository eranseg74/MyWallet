package com.sgl.mywallet;

/**
 * The OverdraftLimitException class represents an exceptional condition
 * in which the withdraw is greater then the balance
 */
public class OverdraftLimitException extends Exception {

    /**
     * Sets up the exception object with a particular message.
     * @param message - The particular message of the exception
     */
    public OverdraftLimitException(String message) { super(message); }

    /**
     * Sets up the exception object with no message
     */
    public OverdraftLimitException() { }
}
