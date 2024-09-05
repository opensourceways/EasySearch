package com.search.common.exception;

public class TrustManagerException extends Exception {
    public TrustManagerException(String msg) {
        super("The error occurred in trustManager, detailed message: " + msg);
    } 
}
