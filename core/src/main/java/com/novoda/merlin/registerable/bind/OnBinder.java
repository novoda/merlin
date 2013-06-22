package com.novoda.merlin.registerable.bind;

import com.novoda.merlin.Log;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.MerlinCallbackManager;
import com.novoda.merlin.registerable.MerlinConnector;

public class OnBinder extends MerlinCallbackManager<Bindable> implements BindListener {

    public OnBinder(MerlinConnector<Bindable> merlinConnector) {
        super(merlinConnector);
    }

    @Override
    public void onMerlinBind(NetworkStatus networkStatus) {
        Log.d("onBind");
        for (Bindable bindable : getRegisterables()) {
            bindable.onBind(networkStatus);
        }
    }
}
