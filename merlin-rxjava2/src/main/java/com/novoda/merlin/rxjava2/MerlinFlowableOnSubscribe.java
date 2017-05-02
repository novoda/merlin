package com.novoda.merlin.rxjava2;

import android.support.annotation.NonNull;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;

class MerlinFlowableOnSubscribe implements FlowableOnSubscribe<NetworkStatus.State> {

    private Merlin merlin;

    MerlinFlowableOnSubscribe(Merlin merlin) {
        this.merlin = merlin;
    }

    @Override
    public void subscribe(@NonNull final FlowableEmitter<NetworkStatus.State> emitter) throws
                                                                                       Exception {
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
            public void cancel() throws
                                 Exception {
                merlin.unbind();
            }
        });

        merlin.bind();
    }
}
