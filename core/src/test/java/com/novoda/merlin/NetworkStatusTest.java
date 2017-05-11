package com.novoda.merlin;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class NetworkStatusTest {

    @Test
    public void givenNewAvailableInstance_whenCheckingIsAvailable_thenReturnsTrue() {
        NetworkStatus networkStatus = NetworkStatus.newAvailableInstance();

        assertThat(networkStatus.isAvailable()).isTrue();
    }

    @Test
    public void givenNewUnavailableInstance_whenCheckingIsAvailable_thenReturnsFalse() {
        NetworkStatus networkStatus = NetworkStatus.newUnavailableInstance();

        assertThat(networkStatus.isAvailable()).isFalse();
    }

}
