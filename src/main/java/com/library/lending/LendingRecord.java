package com.library.lending;

import com.library.book.BookCopy;
import com.library.patron.Patron;

import java.time.LocalDate;

/** A single checkout transaction: which copy, which patron, and its dates. */
public class LendingRecord {

    private final String transactionId;
    private final BookCopy copy;
    private final Patron patron;
    private final LocalDate checkoutDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public LendingRecord(String transactionId, BookCopy copy, Patron patron,
                          LocalDate checkoutDate, LocalDate dueDate) {
        this.transactionId = transactionId;
        this.copy = copy;
        this.patron = patron;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BookCopy getCopy() {
        return copy;
    }

    public Patron getPatron() {
        return patron;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isOverdue(LocalDate asOf) {
        return !isReturned() && asOf.isAfter(dueDate);
    }

    void markReturned(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
