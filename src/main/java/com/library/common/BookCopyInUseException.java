package com.library.common;

/** Thrown when a book or copy can't be removed because it's still checked out. */
public class BookCopyInUseException extends LibraryException {

    public BookCopyInUseException(String message) {
        super(message);
    }
}
