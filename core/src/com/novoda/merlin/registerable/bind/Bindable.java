package com.novoda.merlin.registerable.bind;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.Registerable;

public interface Bindable extends Registerable {
    void onBind(NetworkStatus networkStatus);
}
