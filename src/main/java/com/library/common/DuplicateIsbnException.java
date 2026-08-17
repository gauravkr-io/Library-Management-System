package com.library.common;

public class DuplicateIsbnException extends LibraryException {

    public DuplicateIsbnException(String isbn) {
        super("A book with ISBN " + isbn + " already exists in the catalog");
    }
}
