package com.search.docsearch.except;

public class ServiceImplException extends ServiceException {
    public ServiceImplException(String msg) {
        super("The error occurred in serviceImpl, detailed message: " + msg);
    }
}
