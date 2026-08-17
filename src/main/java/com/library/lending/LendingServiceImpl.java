package com.library.lending;

import com.library.book.BookCopy;
import com.library.book.BookRepository;
import com.library.common.BookCopyNotAvailableException;
import com.library.common.BookNotFoundException;
import com.library.common.InvalidLendingOperationException;
import com.library.common.PatronNotFoundException;
import com.library.notification.LendingEvent;
import com.library.notification.LendingEventListener;
import com.library.notification.LendingEventType;
import com.library.patron.Patron;
import com.library.patron.PatronRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class LendingServiceImpl implements LendingService {

    private static final Logger LOGGER = Logger.getLogger(LendingServiceImpl.class.getName());
    private static final int LOAN_PERIOD_DAYS = 14;

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final LendingRepository lendingRepository;
    private final List<LendingEventListener> listeners = new CopyOnWriteArrayList<>();

    public LendingServiceImpl(BookRepository bookRepository,
                               PatronRepository patronRepository,
                               LendingRepository lendingRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.lendingRepository = lendingRepository;
    }

    @Override
    public LendingRecord checkout(String copyId, String patronId) {
        BookCopy copy = bookRepository.findCopyById(copyId)
                .orElseThrow(() -> new BookNotFoundException(copyId));
        if (!copy.isAvailable()) {
            throw new BookCopyNotAvailableException(copyId);
        }
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronNotFoundException(patronId));

        copy.markCheckedOut();

        LocalDate checkoutDate = LocalDate.now();
        LendingRecord record = new LendingRecord(
                UUID.randomUUID().toString(), copy, patron, checkoutDate, checkoutDate.plusDays(LOAN_PERIOD_DAYS));
        lendingRepository.save(record);

        LOGGER.info(() -> "Checked out copy " + copyId + " to patron " + patronId);
        notifyListeners(new LendingEvent(LendingEventType.CHECKED_OUT, record));
        return record;
    }

    @Override
    public LendingRecord returnCopy(String copyId) {
        if (lendingRepository.findActiveByCopyId(copyId).isEmpty()) {
            throw new InvalidLendingOperationException("Copy " + copyId + " is not currently checked out");
        }
        BookCopy copy = bookRepository.findCopyById(copyId)
                .orElseThrow(() -> new BookNotFoundException(copyId));

        copy.markAvailable();
        LendingRecord record = lendingRepository.markReturned(copyId, LocalDate.now());

        LOGGER.info(() -> "Copy " + copyId + " returned");
        notifyListeners(new LendingEvent(LendingEventType.RETURNED, record));
        return record;
    }

    @Override
    public List<LendingRecord> getBorrowingHistory(String patronId) {
        if (patronRepository.findById(patronId).isEmpty()) {
            throw new PatronNotFoundException(patronId);
        }
        return lendingRepository.findByPatronId(patronId);
    }

    @Override
    public List<LendingRecord> getActiveLoans() {
        return lendingRepository.findActiveLoans();
    }

    @Override
    public void registerListener(LendingEventListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(LendingEvent event) {
        for (LendingEventListener listener : listeners) {
            listener.onLendingEvent(event);
        }
    }
}
