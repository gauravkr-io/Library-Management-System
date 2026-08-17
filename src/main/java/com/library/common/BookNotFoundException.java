package com.library.common;

public class BookNotFoundException extends LibraryException {

    public BookNotFoundException(String isbn) {
        super("No book found with ISBN " + isbn);
    }
}
