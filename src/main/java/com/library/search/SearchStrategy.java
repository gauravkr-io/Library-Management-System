package com.library.search;

import com.library.book.Book;

/** Matches a search query against a book for one field (title, author, ISBN...). */
public interface SearchStrategy {

    boolean matches(Book book, String query);
}
