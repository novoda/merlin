package com.novoda.merlin;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class RxCallbacksManager {

    private final BehaviorSubject<Merlin.ConnectionStatus> merlinRxConnectionStatusSubject;

    public RxCallbacksManager() {
        merlinRxConnectionStatusSubject = BehaviorSubject.create();
    }

    public Observable<Merlin.ConnectionStatus> getRxConnectionStatusObservable() {
        return merlinRxConnectionStatusSubject.asObservable();
    }

    public void onConnect() {
        merlinRxConnectionStatusSubject.onNext(Merlin.ConnectionStatus.CONNECTED);
    }

    public void onDisconnect() {
        merlinRxConnectionStatusSubject.onNext(Merlin.ConnectionStatus.DISCONNECTED);
    }
}
