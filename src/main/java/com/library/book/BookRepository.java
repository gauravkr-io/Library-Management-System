package com.library.book;

import java.util.List;
import java.util.Optional;

/** Storage for books and their copies. */
public interface BookRepository {

    void addBook(Book book);

    Optional<Book> findBookByIsbn(String isbn);

    List<Book> findAllBooks();

    void removeBook(String isbn);

    void addCopy(BookCopy copy);

    Optional<BookCopy> findCopyById(String copyId);

    List<BookCopy> findCopiesByIsbn(String isbn);

    void removeCopy(String copyId);
}
