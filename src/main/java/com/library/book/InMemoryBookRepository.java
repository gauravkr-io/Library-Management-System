package com.library.book;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryBookRepository implements BookRepository {

    private final Map<String, Book> booksByIsbn = new ConcurrentHashMap<>();
    private final Map<String, BookCopy> copiesById = new ConcurrentHashMap<>();

    @Override
    public void addBook(Book book) {
        booksByIsbn.put(book.getIsbn(), book);
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        return Optional.ofNullable(booksByIsbn.get(isbn));
    }

    @Override
    public List<Book> findAllBooks() {
        return List.copyOf(booksByIsbn.values());
    }

    @Override
    public void removeBook(String isbn) {
        booksByIsbn.remove(isbn);
    }

    @Override
    public void addCopy(BookCopy copy) {
        copiesById.put(copy.getCopyId(), copy);
    }

    @Override
    public Optional<BookCopy> findCopyById(String copyId) {
        return Optional.ofNullable(copiesById.get(copyId));
    }

    @Override
    public List<BookCopy> findCopiesByIsbn(String isbn) {
        return copiesById.values().stream()
                .filter(copy -> copy.getBook().getIsbn().equals(isbn))
                .collect(Collectors.toList());
    }

    @Override
    public void removeCopy(String copyId) {
        copiesById.remove(copyId);
    }
}
