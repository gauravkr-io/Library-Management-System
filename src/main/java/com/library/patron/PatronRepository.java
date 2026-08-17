package com.library.patron;

import java.util.List;
import java.util.Optional;

public interface PatronRepository {

    void addPatron(Patron patron);

    Optional<Patron> findById(String patronId);

    List<Patron> findAll();

    void removePatron(String patronId);
}
