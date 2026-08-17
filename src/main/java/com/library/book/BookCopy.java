package com.library.book;

/** A physical copy of a Book. Availability is tracked per copy, not per title. */
public class BookCopy {

    private final String copyId;
    private final Book book;
    private BookStatus status;

    public BookCopy(String copyId, Book book) {
        this.copyId = copyId;
        this.book = book;
        this.status = BookStatus.AVAILABLE;
    }

    public String getCopyId() {
        return copyId;
    }

    public Book getBook() {
        return book;
    }

    public BookStatus getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }

    public void markCheckedOut() {
        if (status == BookStatus.CHECKED_OUT) {
            throw new IllegalStateException("Copy " + copyId + " is already checked out");
        }
        status = BookStatus.CHECKED_OUT;
    }

    public void markAvailable() {
        status = BookStatus.AVAILABLE;
    }
}
