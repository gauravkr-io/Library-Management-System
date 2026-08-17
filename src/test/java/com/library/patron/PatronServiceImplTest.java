package com.library.patron;

import com.library.common.PatronNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PatronServiceImplTest {

    private PatronService patronService;

    @BeforeEach
    void setUp() {
        patronService = new PatronServiceImpl(new InMemoryPatronRepository());
    }

    @Test
    void registerPatron_makesThemRetrievable() {
        Patron patron = patronService.registerPatron("Alice Chen", "alice@example.com");

        Patron found = patronService.getPatron(patron.getPatronId()).orElseThrow();
        assertEquals("Alice Chen", found.getName());
    }

    @Test
    void updatePatron_changesContactInfo() {
        Patron patron = patronService.registerPatron("Alice Chen", "alice@example.com");

        patronService.updatePatron(patron.getPatronId(), "Alice C.", "alice.c@example.com");

        Patron updated = patronService.getPatron(patron.getPatronId()).orElseThrow();
        assertEquals("Alice C.", updated.getName());
        assertEquals("alice.c@example.com", updated.getEmail());
    }

    @Test
    void updatePatron_unknownId_throws() {
        assertThrows(PatronNotFoundException.class,
                () -> patronService.updatePatron("does-not-exist", "X", "x@example.com"));
    }

    @Test
    void getAllPatrons_returnsEveryRegisteredPatron() {
        patronService.registerPatron("Alice Chen", "alice@example.com");
        patronService.registerPatron("Bob Martinez", "bob@example.com");

        assertEquals(2, patronService.getAllPatrons().size());
    }
}
