package com.novoda.merlin.registerable.bind;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.CallbacksRegister;
import com.novoda.support.Logger;

public class Binder extends MerlinCallbackManager<Bindable> implements BindListener {

    public Binder(CallbacksRegister<Bindable> callbacksRegister) {
        super(callbacksRegister);
    }

    @Override
    public void onMerlinBind(NetworkStatus networkStatus) {
        Logger.d("onBind");
        for (Bindable bindable : getRegisterables()) {
            bindable.onBind(networkStatus);
        }
    }
}
