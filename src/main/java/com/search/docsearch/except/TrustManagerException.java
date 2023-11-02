package com.search.docsearch.except;

public class TrustManagerException extends Exception {
    public TrustManagerException(String msg) {
        super("The error occurred in trustManager, detailed message: " + msg);
    } 
}
