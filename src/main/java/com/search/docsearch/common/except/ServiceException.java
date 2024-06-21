package com.search.docsearch.common.except;

public class ServiceException extends ControllerException {
    public ServiceException(String msg) {
        super("The error occurred in service, detailed message: " + msg);
    }
}
