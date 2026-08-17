package com.library.patron;

import java.util.List;
import java.util.Optional;

public interface PatronService {

    Patron registerPatron(String name, String email);

    Patron updatePatron(String patronId, String name, String email);

    Optional<Patron> getPatron(String patronId);

    List<Patron> getAllPatrons();
}
