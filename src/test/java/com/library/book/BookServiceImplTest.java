package com.library.book;

import com.library.common.BookCopyInUseException;
import com.library.common.BookNotFoundException;
import com.library.common.DuplicateIsbnException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookServiceImplTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(new InMemoryBookRepository());
    }

    @Test
    void addBook_makesItRetrievable() {
        bookService.addBook("111", "Dune", "Frank Herbert", 1965);

        Book book = bookService.getBook("111").orElseThrow();
        assertEquals("Dune", book.getTitle());
        assertEquals("Frank Herbert", book.getAuthor());
    }

    @Test
    void addBook_duplicateIsbn_throws() {
        bookService.addBook("111", "Dune", "Frank Herbert", 1965);

        assertThrows(DuplicateIsbnException.class,
                () -> bookService.addBook("111", "Dune Messiah", "Frank Herbert", 1969));
    }

    @Test
    void addCopy_increasesAvailableCount() {
        bookService.addBook("111", "Dune", "Frank Herbert", 1965);

        bookService.addCopy("111");
        bookService.addCopy("111");

        assertEquals(2, bookService.availableCopyCount("111"));
    }

    @Test
    void addCopy_unknownIsbn_throws() {
        assertThrows(BookNotFoundException.class, () -> bookService.addCopy("does-not-exist"));
    }

    @Test
    void updateBook_changesTitleAndAuthor() {
        bookService.addBook("111", "Dune", "Frank Herbert", 1965);

        bookService.updateBook("111", "Dune (Deluxe Edition)", "Frank Herbert", 1965);

        assertEquals("Dune (Deluxe Edition)", bookService.getBook("111").orElseThrow().getTitle());
    }

    @Test
    void removeBook_withNoCheckedOutCopies_removesBookAndCopies() {
        bookService.addBook("111", "Dune", "Frank Herbert", 1965);
        bookService.addCopy("111");

        bookService.removeBook("111");

        assertTrue(bookService.getBook("111").isEmpty());
        assertTrue(bookService.getCopies("111").isEmpty());
    }

    @Test
    void removeBook_withCheckedOutCopy_throws() {
        bookService.addBook("111", "Dune", "Frank Herbert", 1965);
        BookCopy copy = bookService.addCopy("111");
        copy.markCheckedOut();

        assertThrows(BookCopyInUseException.class, () -> bookService.removeBook("111"));
    }

    @Test
    void removeCopy_checkedOutCopy_throws() {
        bookService.addBook("111", "Dune", "Frank Herbert", 1965);
        BookCopy copy = bookService.addCopy("111");
        copy.markCheckedOut();

        assertThrows(BookCopyInUseException.class, () -> bookService.removeCopy(copy.getCopyId()));
    }
}
