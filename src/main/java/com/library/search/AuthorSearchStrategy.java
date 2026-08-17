package com.library.search;

import com.library.book.Book;

public class AuthorSearchStrategy implements SearchStrategy {

    @Override
    public boolean matches(Book book, String query) {
        return book.getAuthor() != null && book.getAuthor().toLowerCase().contains(query.toLowerCase());
    }
}
