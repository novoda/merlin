package com.novoda.merlin;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

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
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(true);

        assertThat(merlinsBeard.isConnectedToWifi()).isTrue();
    }

    @Test
    public void returnFalseForIsConnectedToWifiWhenNetworkConnectedToWifiIsNotConnected() {
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(false);

        assertThat(merlinsBeard.isConnectedToWifi()).isFalse();
    }

    @Test
    public void returnFalseForIsConnectedToWifiWhenNetworkConnectionIsNotAvailable() {
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(null);

        assertThat(merlinsBeard.isConnectedToWifi()).isFalse();
    }

    @Test
    public void returnTrueForIsConnectedToMobileWhenNetworkConnectedToMobileIsConnected() {
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(true);

        assertThat(merlinsBeard.isConnectedToMobileNetwork()).isTrue();
    }

    @Test
    public void returnFalseForIsConnectedToMobileWhenNetworkConnectedToMobileIsNotConnected() {
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(false);

        assertThat(merlinsBeard.isConnectedToMobileNetwork()).isFalse();
    }

    @Test
    public void returnFalseForIsConnectedToMobileWhenNetworkConnectionIsNotAvailable() {
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(null);

        assertThat(merlinsBeard.isConnectedToMobileNetwork()).isFalse();
    }

}
