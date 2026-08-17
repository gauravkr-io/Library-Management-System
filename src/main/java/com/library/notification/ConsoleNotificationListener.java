package com.library.notification;

import com.library.lending.LendingRecord;

/** Stand-in for a real notification channel (email, SMS, push). */
public class ConsoleNotificationListener implements LendingEventListener {

    @Override
    public void onLendingEvent(LendingEvent event) {
        LendingRecord record = event.getRecord();
        String patronName = record.getPatron().getName();
        String title = record.getCopy().getBook().getTitle();

        if (event.getType() == LendingEventType.CHECKED_OUT) {
            System.out.printf("Hi %s, you've checked out '%s'. It's due back on %s.%n",
                    patronName, title, record.getDueDate());
        } else {
            System.out.printf("Thanks %s, we've received your return of '%s'.%n", patronName, title);
        }
    }
}
