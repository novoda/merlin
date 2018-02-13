package com.novoda.merlin;

public class BindCallbackManager extends MerlinCallbackManager<Bindable> {

    public BindCallbackManager(Register<Bindable> register) {
        super(register);
    }

    public void onMerlinBind(NetworkStatus networkStatus) {
        Logger.d("onBind");
        for (Bindable bindable : registerables()) {
            bindable.onBind(networkStatus);
        }
    }
}
