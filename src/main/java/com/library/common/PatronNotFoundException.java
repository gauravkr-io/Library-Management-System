package com.library.common;

public class PatronNotFoundException extends LibraryException {

    public PatronNotFoundException(String patronId) {
        super("No patron found with id " + patronId);
    }
}
