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
    public void subscribe(@NonNull FlowableEmitter<NetworkStatus.State> emitter) throws Exception {
        merlin.registerConnectable(createConnectable(emitter));
        merlin.registerDisconnectable(createDisconnectable(emitter));
        merlin.registerBindable(createBindable(emitter));

        emitter.setCancellable(createCancellable());

        merlin.bind();
    }

    private Connectable createConnectable(final FlowableEmitter<NetworkStatus.State> emitter) {
        return new Connectable() {
            @Override
            public void onConnect() {
                emitter.onNext(NetworkStatus.State.AVAILABLE);
            }
        };
    }

    private Disconnectable createDisconnectable(final FlowableEmitter<NetworkStatus.State> emitter) {
        return new Disconnectable() {
            @Override
            public void onDisconnect() {
                emitter.onNext(NetworkStatus.State.UNAVAILABLE);
            }
        };
    }

    private Bindable createBindable(final FlowableEmitter<NetworkStatus.State> emitter) {
        return new Bindable() {
            @Override
            public void onBind(NetworkStatus networkStatus) {
                NetworkStatus.State current = networkStatus.isAvailable() ? NetworkStatus.State.AVAILABLE : NetworkStatus.State.UNAVAILABLE;
                emitter.onNext(current);
            }
        };
    }

    private Cancellable createCancellable() {
        return new Cancellable() {
            @Override
            public void cancel() throws Exception {
                merlin.unbind();
            }
        };
    }
}
