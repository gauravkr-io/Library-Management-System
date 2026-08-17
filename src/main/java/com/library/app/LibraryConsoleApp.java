package com.library.app;

import com.library.book.Book;
import com.library.book.BookCopy;
import com.library.book.BookService;
import com.library.lending.LendingRecord;
import com.library.lending.LendingService;
import com.library.patron.Patron;
import com.library.patron.PatronService;
import com.library.search.SearchField;
import com.library.search.SearchService;

import java.util.List;
import java.util.Scanner;

/** Text menu for exercising the library system: add/update/remove books and patrons, search, checkout, return. */
public class LibraryConsoleApp {

    private final BookService bookService;
    private final PatronService patronService;
    private final LendingService lendingService;
    private final SearchService searchService;
    private final Scanner scanner = new Scanner(System.in);

    public LibraryConsoleApp(BookService bookService, PatronService patronService,
                              LendingService lendingService, SearchService searchService) {
        this.bookService = bookService;
        this.patronService = patronService;
        this.lendingService = lendingService;
        this.searchService = searchService;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            switch (readInt("Choose an option: ")) {
                case 1 -> addBook();
                case 2 -> updateBook();
                case 3 -> removeBook();
                case 4 -> addCopy();
                case 5 -> removeCopy();
                case 6 -> listBooks();
                case 7 -> searchBooks();
                case 8 -> registerPatron();
                case 9 -> updatePatron();
                case 10 -> listPatrons();
                case 11 -> checkoutBook();
                case 12 -> returnBook();
                case 13 -> viewBorrowingHistory();
                case 0 -> running = false;
                default -> System.out.println("Not a valid option.");
            }
        }
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== Library Management System =====");
        System.out.println(" 1. Add book");
        System.out.println(" 2. Update book");
        System.out.println(" 3. Remove book");
        System.out.println(" 4. Add a copy");
        System.out.println(" 5. Remove a copy");
        System.out.println(" 6. List all books");
        System.out.println(" 7. Search books");
        System.out.println(" 8. Register patron");
        System.out.println(" 9. Update patron");
        System.out.println("10. List all patrons");
        System.out.println("11. Checkout a book");
        System.out.println("12. Return a book");
        System.out.println("13. View borrowing history");
        System.out.println(" 0. Exit");
    }

    private void addBook() {
        String isbn = readLine("ISBN: ");
        String title = readLine("Title: ");
        String author = readLine("Author: ");
        int year = readInt("Publication year: ");

        try {
            Book book = bookService.addBook(isbn, title, author, year);
            System.out.println("Added: " + book);
        } catch (RuntimeException e) {
            System.out.println("Could not add book: " + e.getMessage());
        }
    }

    private void updateBook() {
        String isbn = readLine("ISBN of book to update: ");
        String title = readLine("New title: ");
        String author = readLine("New author: ");
        int year = readInt("New publication year: ");

        try {
            Book book = bookService.updateBook(isbn, title, author, year);
            System.out.println("Updated: " + book);
        } catch (RuntimeException e) {
            System.out.println("Could not update book: " + e.getMessage());
        }
    }

    private void removeBook() {
        String isbn = readLine("ISBN of book to remove: ");
        try {
            bookService.removeBook(isbn);
            System.out.println("Removed book " + isbn);
        } catch (RuntimeException e) {
            System.out.println("Could not remove book: " + e.getMessage());
        }
    }

    private void addCopy() {
        String isbn = readLine("ISBN to add a copy of: ");
        try {
            BookCopy copy = bookService.addCopy(isbn);
            System.out.println("Added copy " + copy.getCopyId());
        } catch (RuntimeException e) {
            System.out.println("Could not add copy: " + e.getMessage());
        }
    }

    private void removeCopy() {
        String copyId = readLine("Copy id to remove: ");
        try {
            bookService.removeCopy(copyId);
            System.out.println("Removed copy " + copyId);
        } catch (RuntimeException e) {
            System.out.println("Could not remove copy: " + e.getMessage());
        }
    }

    private void listBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books in the catalog.");
            return;
        }
        for (Book book : books) {
            long available = bookService.availableCopyCount(book.getIsbn());
            int total = bookService.getCopies(book.getIsbn()).size();
            System.out.println(book + " | " + available + "/" + total + " copies available");
        }
    }

    private void searchBooks() {
        System.out.println("Search by: 1) Title  2) Author  3) ISBN");
        SearchField field = switch (readInt("Choice: ")) {
            case 1 -> SearchField.TITLE;
            case 2 -> SearchField.AUTHOR;
            default -> SearchField.ISBN;
        };
        String query = readLine("Search text: ");

        List<Book> results = searchService.search(field, query);
        if (results.isEmpty()) {
            System.out.println("No matches.");
        } else {
            results.forEach(System.out::println);
        }
    }

    private void registerPatron() {
        String name = readLine("Name: ");
        String email = readLine("Email: ");
        try {
            Patron patron = patronService.registerPatron(name, email);
            System.out.println("Registered: " + patron);
        } catch (RuntimeException e) {
            System.out.println("Could not register patron: " + e.getMessage());
        }
    }

    private void updatePatron() {
        String patronId = readLine("Patron id: ");
        String name = readLine("New name: ");
        String email = readLine("New email: ");
        try {
            Patron patron = patronService.updatePatron(patronId, name, email);
            System.out.println("Updated: " + patron);
        } catch (RuntimeException e) {
            System.out.println("Could not update patron: " + e.getMessage());
        }
    }

    private void listPatrons() {
        List<Patron> patrons = patronService.getAllPatrons();
        if (patrons.isEmpty()) {
            System.out.println("No patrons registered.");
            return;
        }
        patrons.forEach(System.out::println);
    }

    private void checkoutBook() {
        String copyId = readLine("Copy id: ");
        String patronId = readLine("Patron id: ");
        try {
            LendingRecord record = lendingService.checkout(copyId, patronId);
            System.out.println("Checked out. Due back " + record.getDueDate());
        } catch (RuntimeException e) {
            System.out.println("Could not check out: " + e.getMessage());
        }
    }

    private void returnBook() {
        String copyId = readLine("Copy id: ");
        try {
            lendingService.returnCopy(copyId);
            System.out.println("Returned copy " + copyId);
        } catch (RuntimeException e) {
            System.out.println("Could not return copy: " + e.getMessage());
        }
    }

    private void viewBorrowingHistory() {
        String patronId = readLine("Patron id: ");
        try {
            List<LendingRecord> history = lendingService.getBorrowingHistory(patronId);
            if (history.isEmpty()) {
                System.out.println("No borrowing history.");
                return;
            }
            for (LendingRecord record : history) {
                String status = record.isReturned() ? "returned " + record.getReturnDate() : "still out";
                System.out.println(record.getCopy().getBook().getTitle()
                        + " | checked out " + record.getCheckoutDate() + " | " + status);
            }
        } catch (RuntimeException e) {
            System.out.println("Could not fetch history: " + e.getMessage());
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Enter a number.");
            }
        }
    }
}
