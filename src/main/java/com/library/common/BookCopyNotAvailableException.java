package com.library.common;

public class BookCopyNotAvailableException extends LibraryException {

    public BookCopyNotAvailableException(String copyId) {
        super("Copy " + copyId + " is not available for checkout");
    }
}
