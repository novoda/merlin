package com.novoda.merlin;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NetworkStatusShould {

    @Test
    public void isAvailableReturnsTrueWhenNetworkStatusNewAvailableInstanceCreated() {
        NetworkStatus networkStatus = NetworkStatus.newAvailableInstance();

        assertTrue(networkStatus.isAvailable());
    }

    @Test
    public void isAvailableReturnsFalseWhenNetworkStatusNewUnavailableInstanceCreated() {
        NetworkStatus networkStatus = NetworkStatus.newUnavailableInstance();

        assertFalse(networkStatus.isAvailable());
    }

}