package com.library.lending;

import com.library.book.Book;
import com.library.book.BookCopy;
import com.library.book.BookRepository;
import com.library.book.BookService;
import com.library.book.BookServiceImpl;
import com.library.book.InMemoryBookRepository;
import com.library.common.BookCopyNotAvailableException;
import com.library.common.InvalidLendingOperationException;
import com.library.notification.LendingEventType;
import com.library.patron.InMemoryPatronRepository;
import com.library.patron.Patron;
import com.library.patron.PatronRepository;
import com.library.patron.PatronService;
import com.library.patron.PatronServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LendingServiceImplTest {

    private LendingService lendingService;
    private BookCopy copy;
    private Patron patron;

    @BeforeEach
    void setUp() {
        BookRepository bookRepository = new InMemoryBookRepository();
        PatronRepository patronRepository = new InMemoryPatronRepository();
        LendingRepository lendingRepository = new InMemoryLendingRepository();

        BookService bookService = new BookServiceImpl(bookRepository);
        PatronService patronService = new PatronServiceImpl(patronRepository);
        lendingService = new LendingServiceImpl(bookRepository, patronRepository, lendingRepository);

        Book book = bookService.addBook("111", "Dune", "Frank Herbert", 1965);
        copy = bookService.addCopy(book.getIsbn());
        patron = patronService.registerPatron("Alice Chen", "alice@example.com");
    }

    @Test
    void checkout_marksCopyUnavailableAndCreatesRecord() {
        LendingRecord record = lendingService.checkout(copy.getCopyId(), patron.getPatronId());

        assertFalse(copy.isAvailable());
        assertEquals(patron.getPatronId(), record.getPatron().getPatronId());
        assertFalse(record.isReturned());
    }

    @Test
    void checkout_alreadyCheckedOutCopy_throws() {
        lendingService.checkout(copy.getCopyId(), patron.getPatronId());

        assertThrows(BookCopyNotAvailableException.class,
                () -> lendingService.checkout(copy.getCopyId(), patron.getPatronId()));
    }

    @Test
    void returnCopy_marksCopyAvailableAndClosesRecord() {
        lendingService.checkout(copy.getCopyId(), patron.getPatronId());

        LendingRecord record = lendingService.returnCopy(copy.getCopyId());

        assertTrue(copy.isAvailable());
        assertTrue(record.isReturned());
    }

    @Test
    void returnCopy_notCheckedOut_throws() {
        assertThrows(InvalidLendingOperationException.class, () -> lendingService.returnCopy(copy.getCopyId()));
    }

    @Test
    void getBorrowingHistory_reflectsPastLoans() {
        lendingService.checkout(copy.getCopyId(), patron.getPatronId());
        lendingService.returnCopy(copy.getCopyId());

        List<LendingRecord> history = lendingService.getBorrowingHistory(patron.getPatronId());

        assertEquals(1, history.size());
        assertTrue(history.get(0).isReturned());
    }

    @Test
    void listeners_areNotifiedOnCheckoutAndReturn() {
        List<LendingEventType> receivedTypes = new ArrayList<>();
        lendingService.registerListener(event -> receivedTypes.add(event.getType()));

        lendingService.checkout(copy.getCopyId(), patron.getPatronId());
        lendingService.returnCopy(copy.getCopyId());

        assertEquals(List.of(LendingEventType.CHECKED_OUT, LendingEventType.RETURNED), receivedTypes);
    }
}
