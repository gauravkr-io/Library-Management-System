package com.library.search;

import com.library.book.Book;
import com.library.book.BookRepository;
import com.library.book.BookService;
import com.library.book.BookServiceImpl;
import com.library.book.InMemoryBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchServiceTest {

    private SearchService searchService;

    @BeforeEach
    void setUp() {
        BookRepository bookRepository = new InMemoryBookRepository();
        BookService bookService = new BookServiceImpl(bookRepository);
        searchService = new SearchService(bookRepository);

        bookService.addBook("111", "The Hobbit", "J.R.R. Tolkien", 1937);
        bookService.addBook("222", "The Fellowship of the Ring", "J.R.R. Tolkien", 1954);
        bookService.addBook("333", "Dune", "Frank Herbert", 1965);
    }

    @Test
    void searchByTitle_isCaseInsensitiveSubstringMatch() {
        List<Book> results = searchService.search(SearchField.TITLE, "hobbit");

        assertEquals(1, results.size());
        assertEquals("The Hobbit", results.get(0).getTitle());
    }

    @Test
    void searchByAuthor_returnsAllMatchingTitles() {
        List<Book> results = searchService.search(SearchField.AUTHOR, "tolkien");

        assertEquals(2, results.size());
    }

    @Test
    void searchByIsbn_isExactMatch() {
        List<Book> results = searchService.search(SearchField.ISBN, "333");

        assertEquals(1, results.size());
        assertEquals("Dune", results.get(0).getTitle());
    }

    @Test
    void search_noMatches_returnsEmptyList() {
        List<Book> results = searchService.search(SearchField.TITLE, "nonexistent");

        assertTrue(results.isEmpty());
    }

    @Test
    void registerStrategy_letsCallerOverrideMatchingWithoutTouchingSearchService() {
        searchService.registerStrategy(SearchField.TITLE, (book, query) -> book.getTitle().equals(query));

        List<Book> results = searchService.search(SearchField.TITLE, "The Hobbit");

        assertEquals(1, results.size());
    }
}
