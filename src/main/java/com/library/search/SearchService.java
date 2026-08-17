package com.library.search;

import com.library.book.Book;
import com.library.book.BookRepository;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchService {

    private final BookRepository bookRepository;
    private final Map<SearchField, SearchStrategy> strategies = new EnumMap<>(SearchField.class);

    public SearchService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        strategies.put(SearchField.TITLE, new TitleSearchStrategy());
        strategies.put(SearchField.AUTHOR, new AuthorSearchStrategy());
        strategies.put(SearchField.ISBN, new IsbnSearchStrategy());
    }

    /** Lets a caller plug in a different matching strategy for a field, or add a new one. */
    public void registerStrategy(SearchField field, SearchStrategy strategy) {
        strategies.put(field, strategy);
    }

    public List<Book> search(SearchField field, String query) {
        SearchStrategy strategy = strategies.get(field);
        if (strategy == null) {
            throw new IllegalArgumentException("No search strategy registered for " + field);
        }
        return bookRepository.findAllBooks().stream()
                .filter(book -> strategy.matches(book, query))
                .collect(Collectors.toList());
    }
}
