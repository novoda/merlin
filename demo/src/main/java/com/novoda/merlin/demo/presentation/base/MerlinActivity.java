package com.novoda.merlin.demo.presentation.base;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import android.app.Activity;
import android.os.Bundle;

import rx.Observable;
import rx.Subscription;

public abstract class MerlinActivity extends Activity {

    protected Merlin merlin;
    protected Observable<Boolean> connectionStatusObservable;
    protected Subscription rxSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        merlin = createMerlin();
        connectionStatusObservable = merlin.getRxConnectionStatusObervable();
    }

    protected abstract Merlin createMerlin();

    protected abstract Subscription createRxSubscription();

    protected void registerConnectable(Connectable connectable) {
        merlin.registerConnectable(connectable);
    }

    protected void registerDisconnectable(Disconnectable disconnectable) {
        merlin.registerDisconnectable(disconnectable);
    }

    protected void registerBindable(Bindable bindable) {
        merlin.registerBindable(bindable);
    }

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
