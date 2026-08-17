package com.library.lending;

import com.library.notification.LendingEventListener;

import java.util.List;

public interface LendingService {

    LendingRecord checkout(String copyId, String patronId);

    LendingRecord returnCopy(String copyId);

    List<LendingRecord> getBorrowingHistory(String patronId);

    List<LendingRecord> getActiveLoans();

    void registerListener(LendingEventListener listener);
}
