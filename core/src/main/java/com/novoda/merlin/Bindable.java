package com.novoda.merlin;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.Registerable;

public interface Bindable extends Registerable {
    void onBind(NetworkStatus networkStatus);
}
