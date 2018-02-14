package com.novoda.merlin;

public interface Bindable extends Registerable {
    void onBind(NetworkStatus networkStatus);
}
