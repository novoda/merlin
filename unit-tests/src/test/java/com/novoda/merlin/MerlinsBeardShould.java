package com.novoda.merlin;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static com.novoda.merlin.MerlinsBeard.NetworkType.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MerlinRobolectricTestRunner.class)
public class MerlinsBeardShould {

    @Mock
    private ConnectivityManager mockConnectivityManager;

    @Mock
    private NetworkInfo mockNetworkInfo;

    private MerlinsBeard merlinsBeard;

    @Before
    public void setUp() {
        initMocks(this);

        merlinsBeard = new MerlinsBeard(mockConnectivityManager);
    }

    @Test
    public void returnFalseForIsConnectedWhenNetworkConnectionIsUnavailable() {
        when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(false);

        assertThat(merlinsBeard.isConnected()).isFalse();
    }

    @Test
    public void returnFalseForIsConnectedWhenNetworkConnectionIsNull() {
        when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(null);

        assertThat(merlinsBeard.isConnected()).isFalse();
    }

    @Test
    public void returnTrueForIsConnectedWhenNetworkConnectionIsAvailable() {
        when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(true);

        assertThat(merlinsBeard.isConnected()).isTrue();
    }

    @Test
    public void returnTrueForIsConnectedToWifiWhenNetworkConnectedToWifiIsConnected() {
        when(mockConnectivityManager.getNetworkInfo(WIFI.getValue())).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(true);

        assertThat(merlinsBeard.isConnectedTo(WIFI)).isTrue();
    }

    @Test
    public void returnFalseForIsConnectedToWifiWhenNetworkConnectedToWifiIsNotConnected() {
        when(mockConnectivityManager.getNetworkInfo(WIFI.getValue())).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(false);

        assertThat(merlinsBeard.isConnectedTo(WIFI)).isFalse();
    }

    @Test
    public void returnFalseForIsConnectedToWifiWhenNetworkConnectionIsNotAvailable() {
        when(mockConnectivityManager.getNetworkInfo(WIFI.getValue())).thenReturn(null);

        assertThat(merlinsBeard.isConnectedTo(WIFI)).isFalse();
    }

}
