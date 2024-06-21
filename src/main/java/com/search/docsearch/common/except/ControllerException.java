package com.search.docsearch.common.except;

public class ControllerException extends Exception {
    public ControllerException(String msg) {
        super("The error occurred in controller, detailed message: " + msg);
    }
}
