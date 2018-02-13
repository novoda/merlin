package com.novoda.merlin;

public class DisconnectCallbackManager extends MerlinCallbackManager<Disconnectable> implements Disconnectable {

    public DisconnectCallbackManager(Register<Disconnectable> register) {
        super(register);
    }

    @Override
    public void onDisconnect() {
        Logger.d("onDisconnect");
        for (Disconnectable disconnectable : registerables()) {
            disconnectable.onDisconnect();
        }
    }
}
