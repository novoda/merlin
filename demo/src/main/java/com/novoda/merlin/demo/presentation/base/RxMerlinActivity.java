package com.novoda.merlin.demo.presentation.base;

import android.app.Activity;
import android.os.Bundle;

import com.novoda.merlin.Merlin;

import rx.Observable;
import rx.Subscription;

public abstract class RxMerlinActivity extends Activity {

    protected Merlin merlin;
    protected Observable<Boolean> connectionStatusObservable;
    protected Subscription rxSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        merlin = createMerlin();
        connectionStatusObservable = merlin.getConnectionStatusObservable();
    }

    protected abstract Merlin createMerlin();

    protected abstract Subscription createRxSubscription();

    @Override
    protected void onStart() {
        super.onStart();
        merlin.bind();
        rxSubscription = createRxSubscription();
    }

    @Override
    protected void onStop() {
        super.onStop();
        merlin.unbind();
        rxSubscription.unsubscribe();
    }

}
