package com.novoda.merlin;

import android.support.annotation.NonNull;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;

class MerlinFlowableOnSubscribe implements FlowableOnSubscribe<NetworkStatus> {

    private Merlin merlin;

    MerlinFlowableOnSubscribe(Merlin merlin) {
        this.merlin = merlin;
    }

    @Override
    public void subscribe(@NonNull FlowableEmitter<NetworkStatus> emitter) throws Exception {
        merlin.registerConnectable(createConnectable(emitter));
        merlin.registerDisconnectable(createDisconnectable(emitter));
        merlin.registerBindable(createBindable(emitter));

        emitter.setCancellable(createCancellable());

        merlin.bind();
    }

    private Connectable createConnectable(final FlowableEmitter<NetworkStatus> emitter) {
        return new Connectable() {
            @Override
            public void onConnect() {
                emitter.onNext(NetworkStatus.newAvailableInstance());
            }
        };
    }

    private Disconnectable createDisconnectable(final FlowableEmitter<NetworkStatus> emitter) {
        return new Disconnectable() {
            @Override
            public void onDisconnect() {
                emitter.onNext(NetworkStatus.newUnavailableInstance());
            }
        };
    }

    private Bindable createBindable(final FlowableEmitter<NetworkStatus> emitter) {
        return new Bindable() {
            @Override
            public void onBind(NetworkStatus current) {
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
