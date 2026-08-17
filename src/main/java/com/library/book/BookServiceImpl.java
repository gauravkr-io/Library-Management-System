package com.library.book;

import com.library.common.BookCopyInUseException;
import com.library.common.BookNotFoundException;
import com.library.common.DuplicateIsbnException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class BookServiceImpl implements BookService {

    private static final Logger LOGGER = Logger.getLogger(BookServiceImpl.class.getName());

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addBook(String isbn, String title, String author, int publicationYear) {
        if (bookRepository.findBookByIsbn(isbn).isPresent()) {
            throw new DuplicateIsbnException(isbn);
        }
        Book book = new Book(isbn, title, author, publicationYear);
        bookRepository.addBook(book);
        LOGGER.info(() -> "Added book '" + title + "' (" + isbn + ") to the catalog");
        return book;
    }

    @Override
    public Book updateBook(String isbn, String title, String author, int publicationYear) {
        Book book = getBookOrThrow(isbn);
        book.updateDetails(title, author, publicationYear);
        LOGGER.info(() -> "Updated book " + isbn);
        return book;
    }

    @Override
    public void removeBook(String isbn) {
        Book book = getBookOrThrow(isbn);
        List<BookCopy> copies = bookRepository.findCopiesByIsbn(isbn);
        boolean anyCheckedOut = copies.stream().anyMatch(copy -> !copy.isAvailable());
        if (anyCheckedOut) {
            throw new BookCopyInUseException(
                    "Cannot remove '" + book.getTitle() + "' - at least one copy is still checked out");
        }
        copies.forEach(copy -> bookRepository.removeCopy(copy.getCopyId()));
        bookRepository.removeBook(isbn);
        LOGGER.info(() -> "Removed book " + isbn + " and its " + copies.size() + " copy(ies)");
    }

    @Override
    public BookCopy addCopy(String isbn) {
        Book book = getBookOrThrow(isbn);
        BookCopy copy = new BookCopy(UUID.randomUUID().toString(), book);
        bookRepository.addCopy(copy);
        LOGGER.info(() -> "Added a new copy of '" + book.getTitle() + "' (copy " + copy.getCopyId() + ")");
        return copy;
    }

    @Override
    public void removeCopy(String copyId) {
        BookCopy copy = bookRepository.findCopyById(copyId)
                .orElseThrow(() -> new BookNotFoundException(copyId));
        if (!copy.isAvailable()) {
            throw new BookCopyInUseException("Cannot remove copy " + copyId + " - it is currently checked out");
        }
        bookRepository.removeCopy(copyId);
        LOGGER.info(() -> "Removed copy " + copyId);
    }

    @Override
    public Optional<Book> getBook(String isbn) {
        return bookRepository.findBookByIsbn(isbn);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAllBooks();
    }

    @Override
    public List<BookCopy> getCopies(String isbn) {
        return bookRepository.findCopiesByIsbn(isbn);
    }

    @Override
    public long availableCopyCount(String isbn) {
        return bookRepository.findCopiesByIsbn(isbn).stream()
                .filter(BookCopy::isAvailable)
                .count();
    }

    private Book getBookOrThrow(String isbn) {
        return bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }
}
