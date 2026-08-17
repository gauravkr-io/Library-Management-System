package com.library.patron;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPatronRepository implements PatronRepository {

    private final Map<String, Patron> patronsById = new ConcurrentHashMap<>();

    @Override
    public void addPatron(Patron patron) {
        patronsById.put(patron.getPatronId(), patron);
    }

    @Override
    public Optional<Patron> findById(String patronId) {
        return Optional.ofNullable(patronsById.get(patronId));
    }

    @Override
    public List<Patron> findAll() {
        return List.copyOf(patronsById.values());
    }

    @Override
    public void removePatron(String patronId) {
        patronsById.remove(patronId);
    }
}
