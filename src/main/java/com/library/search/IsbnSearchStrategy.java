package com.library.search;

import com.library.book.Book;

public class IsbnSearchStrategy implements SearchStrategy {

    @Override
    public boolean matches(Book book, String query) {
        return book.getIsbn().equalsIgnoreCase(query);
    }
}
