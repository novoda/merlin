package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Cancellable;

public class MerlinObservable {

    public static Observable<NetworkStatus.State> from(Context context) {
        MerlinBuilder builder = new MerlinBuilder();
        builder.withAllCallbacks();
        final Merlin merlin = builder.build(context);

        return Observable.create(new Action1<Emitter<NetworkStatus.State>>() {
            @Override
            public void call(final Emitter<NetworkStatus.State> emitter) {
                merlin.registerConnectable(new Connectable() {
                    @Override
                    public void onConnect() {
                        emitter.onNext(NetworkStatus.State.AVAILABLE);
                    }
                });
                merlin.registerDisconnectable(new Disconnectable() {
                    @Override
                    public void onDisconnect() {
                        emitter.onNext(NetworkStatus.State.UNAVAILABLE);
                    }
                });
                merlin.registerBindable(new Bindable() {
                    @Override
                    public void onBind(NetworkStatus networkStatus) {
                        NetworkStatus.State current = networkStatus.isAvailable() ? NetworkStatus.State.AVAILABLE : NetworkStatus.State.UNAVAILABLE;
                        emitter.onNext(current);
                    }
                });

                emitter.setCancellation(new Cancellable() {
                    @Override
                    public void cancel() throws
                                         Exception {
                        merlin.unbind();
                    }
                });

                merlin.bind();
            }
        }, Emitter.BackpressureMode.BUFFER)
                .distinctUntilChanged();
    }
}
