package com.search.docsearch.common.except;

public class TrustManagerException extends Exception {
    public TrustManagerException(String msg) {
        super("The error occurred in trustManager, detailed message: " + msg);
    }
}
