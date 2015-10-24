package com.novoda.merlin;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class RxCallbacksManager {

    private final BehaviorSubject<Boolean> merlinRxConnectionStatusSubject;

    public RxCallbacksManager() {
        merlinRxConnectionStatusSubject = BehaviorSubject.create();
    }

    public Observable<Boolean> getRxConnectionStatusObservable() {
        return merlinRxConnectionStatusSubject.asObservable();
    }

    public void onConnect() {
        merlinRxConnectionStatusSubject.onNext(true);
    }

    public void onDisconnect() {
        merlinRxConnectionStatusSubject.onNext(false);
    }
}
