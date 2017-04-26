package com.novoda.merlin.rxjava2;

import android.content.Context;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinBuilder;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Cancellable;

public class MerlinFlowable {

    public static Flowable<NetworkStatus.State> from(Context context) {
        MerlinBuilder builder = new Merlin.Builder();
        builder.withAllCallbacks();
        final Merlin merlin = builder.build(context);

        return Flowable.create(new FlowableOnSubscribe<NetworkStatus.State>() {
            @Override
            public void subscribe(@NonNull final FlowableEmitter<NetworkStatus.State> emitter) throws Exception {
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
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        merlin.unbind();
                    }
                });

                merlin.bind();

            }
        }, BackpressureStrategy.BUFFER)
                       .distinctUntilChanged();
    }
}
