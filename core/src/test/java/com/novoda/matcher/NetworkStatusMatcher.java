package com.novoda.matcher;

import com.novoda.merlin.NetworkStatus;

import org.mockito.ArgumentMatcher;

import static org.mockito.Matchers.argThat;

class NetworkStatusMatcher implements ArgumentMatcher<NetworkStatus> {

    private final NetworkStatus expected;

    public static NetworkStatus match(NetworkStatus networkStatus) {
        return argThat(new NetworkStatusMatcher(networkStatus));
    }

    private NetworkStatusMatcher(NetworkStatus expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(NetworkStatus argument) {
        return argument.equals(expected);
    }
}
