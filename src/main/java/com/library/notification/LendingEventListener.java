package com.library.notification;

/** Observer that reacts to checkout/return events fired by LendingService. */
public interface LendingEventListener {

    void onLendingEvent(LendingEvent event);
}
