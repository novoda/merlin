package com.novoda.merlin.demo.connectivity.display;

public interface NetworkStatusDisplayer {
    void displayConnected();

    void displayDisconnected();

    void reset();

    void displayNetworkSubtype(String mobileNetworkSubtypeName);
}

