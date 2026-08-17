package com.library.patron;

import com.library.common.PatronNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class PatronServiceImpl implements PatronService {

    private static final Logger LOGGER = Logger.getLogger(PatronServiceImpl.class.getName());

    private final PatronRepository patronRepository;

    public PatronServiceImpl(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    @Override
    public Patron registerPatron(String name, String email) {
        Patron patron = new Patron(UUID.randomUUID().toString(), name, email);
        patronRepository.addPatron(patron);
        LOGGER.info(() -> "Registered new patron '" + name + "' (" + patron.getPatronId() + ")");
        return patron;
    }

    @Override
    public Patron updatePatron(String patronId, String name, String email) {
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronNotFoundException(patronId));
        patron.updateContactInfo(name, email);
        LOGGER.info(() -> "Updated patron " + patronId);
        return patron;
    }

    @Override
    public Optional<Patron> getPatron(String patronId) {
        return patronRepository.findById(patronId);
    }

    @Override
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }
}
