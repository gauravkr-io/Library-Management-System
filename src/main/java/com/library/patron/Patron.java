package com.library.patron;

import java.util.Objects;

public class Patron {

    private final String patronId;
    private String name;
    private String email;

    public Patron(String patronId, String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.patronId = patronId;
        this.name = name;
        this.email = email;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    void updateContactInfo(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patron patron)) return false;
        return patronId.equals(patron.patronId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patronId);
    }

    @Override
    public String toString() {
        return name + " <" + email + "> (" + patronId + ")";
    }
}
