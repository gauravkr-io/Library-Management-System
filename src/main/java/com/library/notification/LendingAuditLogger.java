package com.library.notification;

import com.library.lending.LendingRecord;

import java.util.logging.Logger;

/** Writes every checkout/return to the log for an audit trail. */
public class LendingAuditLogger implements LendingEventListener {

    private static final Logger LOGGER = Logger.getLogger(LendingAuditLogger.class.getName());

    @Override
    public void onLendingEvent(LendingEvent event) {
        LendingRecord record = event.getRecord();
        switch (event.getType()) {
            case CHECKED_OUT -> LOGGER.info(() -> String.format(
                    "AUDIT: copy %s checked out to patron %s, due %s",
                    record.getCopy().getCopyId(), record.getPatron().getPatronId(), record.getDueDate()));
            case RETURNED -> LOGGER.info(() -> String.format(
                    "AUDIT: copy %s returned by patron %s on %s",
                    record.getCopy().getCopyId(), record.getPatron().getPatronId(), record.getReturnDate()));
        }
    }
}
