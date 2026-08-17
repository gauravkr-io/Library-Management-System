package com.library.common;

/** Base class for all domain errors thrown by the library services. */
public abstract class LibraryException extends RuntimeException {

    protected LibraryException(String message) {
        super(message);
    }
}
