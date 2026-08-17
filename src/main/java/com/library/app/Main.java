package com.library.app;

import com.library.book.Book;
import com.library.book.BookRepository;
import com.library.book.BookService;
import com.library.book.BookServiceImpl;
import com.library.book.InMemoryBookRepository;
import com.library.lending.InMemoryLendingRepository;
import com.library.lending.LendingRepository;
import com.library.lending.LendingService;
import com.library.lending.LendingServiceImpl;
import com.library.notification.ConsoleNotificationListener;
import com.library.notification.LendingAuditLogger;
import com.library.patron.InMemoryPatronRepository;
import com.library.patron.Patron;
import com.library.patron.PatronRepository;
import com.library.patron.PatronService;
import com.library.patron.PatronServiceImpl;
import com.library.search.SearchService;

/** Wires up the services and starts the console menu. */
public class Main {

    public static void main(String[] args) {
        BookRepository bookRepository = new InMemoryBookRepository();
        PatronRepository patronRepository = new InMemoryPatronRepository();
        LendingRepository lendingRepository = new InMemoryLendingRepository();

        BookService bookService = new BookServiceImpl(bookRepository);
        PatronService patronService = new PatronServiceImpl(patronRepository);
        SearchService searchService = new SearchService(bookRepository);

        LendingService lendingService = new LendingServiceImpl(bookRepository, patronRepository, lendingRepository);
        lendingService.registerListener(new LendingAuditLogger());
        lendingService.registerListener(new ConsoleNotificationListener());

        loadSampleData(bookService, patronService);

        new LibraryConsoleApp(bookService, patronService, lendingService, searchService).run();
    }

    // A couple of starter records so the menu isn't empty on first run.
    private static void loadSampleData(BookService bookService, PatronService patronService) {
        Book hobbit = bookService.addBook("978-0-618-00221-4", "The Hobbit", "J.R.R. Tolkien", 1937);
        bookService.addBook("978-0-13-468599-1", "Effective Java", "Joshua Bloch", 2018);
        bookService.addCopy(hobbit.getIsbn());
        bookService.addCopy(hobbit.getIsbn());

        Patron alice = patronService.registerPatron("Alice Chen", "alice@example.com");

        System.out.println("Sample data loaded:");
        System.out.println("  " + hobbit);
        System.out.println("  " + alice);
    }
}
