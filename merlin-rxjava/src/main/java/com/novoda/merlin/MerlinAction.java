package com.novoda.merlin;

import rx.Emitter;
import rx.functions.Action1;
import rx.functions.Cancellable;

class MerlinAction implements Action1<Emitter<NetworkStatus>> {

    private Merlin merlin;

    MerlinAction(Merlin merlin) {
        this.merlin = merlin;
    }

    @Override
    public void call(Emitter<NetworkStatus> stateEmitter) {
        merlin.registerConnectable(createConnectable(stateEmitter));
        merlin.registerDisconnectable(createDisconnectable(stateEmitter));
        merlin.registerBindable(createBindable(stateEmitter));

        stateEmitter.setCancellation(createCancellable());

        merlin.bind();
    }

    private Connectable createConnectable(final Emitter<NetworkStatus> stateEmitter) {
        return new Connectable() {
            @Override
            public void onConnect() {
                stateEmitter.onNext(NetworkStatus.newAvailableInstance());
            }
        };
    }

    private Disconnectable createDisconnectable(final Emitter<NetworkStatus> stateEmitter) {
        return new Disconnectable() {
            @Override
            public void onDisconnect() {
                stateEmitter.onNext(NetworkStatus.newUnavailableInstance());
            }
        };
    }

    private Bindable createBindable(final Emitter<NetworkStatus> stateEmitter) {
        return new Bindable() {
            @Override
            public void onBind(NetworkStatus current) {
                stateEmitter.onNext(current);
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
