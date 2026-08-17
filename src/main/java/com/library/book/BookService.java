package com.library.book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book addBook(String isbn, String title, String author, int publicationYear);

    Book updateBook(String isbn, String title, String author, int publicationYear);

    void removeBook(String isbn);

    BookCopy addCopy(String isbn);

    void removeCopy(String copyId);

    Optional<Book> getBook(String isbn);

    List<Book> getAllBooks();

    List<BookCopy> getCopies(String isbn);

    long availableCopyCount(String isbn);
}
