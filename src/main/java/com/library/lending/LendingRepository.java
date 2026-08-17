package com.library.lending;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LendingRepository {

    void save(LendingRecord record);

    Optional<LendingRecord> findActiveByCopyId(String copyId);

    /** Closes the active loan for a copy. */
    LendingRecord markReturned(String copyId, LocalDate returnDate);

    List<LendingRecord> findByPatronId(String patronId);

    List<LendingRecord> findActiveLoans();

    List<LendingRecord> findAll();
}
