package com.novoda.merlin;

public class ConnectCallbackManager extends MerlinCallbackManager<Connectable> {

    public ConnectCallbackManager(Register<Connectable> register) {
        super(register);
    }

    public void onConnect() {
        Logger.d("onConnect");
        for (Connectable connectable : registerables()) {
            connectable.onConnect();
        }
    }

}
