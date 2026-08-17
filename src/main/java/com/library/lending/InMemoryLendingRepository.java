package com.library.lending;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryLendingRepository implements LendingRepository {

    private final Map<String, LendingRecord> recordsByTransactionId = new ConcurrentHashMap<>();
    private final Map<String, LendingRecord> activeByCopyId = new ConcurrentHashMap<>();

    @Override
    public void save(LendingRecord record) {
        recordsByTransactionId.put(record.getTransactionId(), record);
        if (!record.isReturned()) {
            activeByCopyId.put(record.getCopy().getCopyId(), record);
        }
    }

    @Override
    public Optional<LendingRecord> findActiveByCopyId(String copyId) {
        return Optional.ofNullable(activeByCopyId.get(copyId));
    }

    @Override
    public LendingRecord markReturned(String copyId, LocalDate returnDate) {
        LendingRecord record = activeByCopyId.remove(copyId);
        if (record == null) {
            throw new IllegalStateException("No active loan for copy " + copyId);
        }
        record.markReturned(returnDate);
        return record;
    }

    @Override
    public List<LendingRecord> findByPatronId(String patronId) {
        return recordsByTransactionId.values().stream()
                .filter(record -> record.getPatron().getPatronId().equals(patronId))
                .collect(Collectors.toList());
    }

    @Override
    public List<LendingRecord> findActiveLoans() {
        return List.copyOf(activeByCopyId.values());
    }

    @Override
    public List<LendingRecord> findAll() {
        return List.copyOf(recordsByTransactionId.values());
    }
}
