package com.novoda.merlin.registerable.bind;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.Register;
import com.novoda.merlin.logger.Logger;

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
