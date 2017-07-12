package com.novoda.merlin;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.novoda.merlin.service.AndroidVersion;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class MerlinsBeardTest {

    private static final boolean DISCONNECTED = false;
    private static final boolean CONNECTED = true;

    private static final boolean BELOW_LOLLIPOP = false;
    private static final boolean LOLLIPOP_OR_ABOVE = true;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ConnectivityManager connectivityManager;
    @Mock
    private NetworkInfo networkInfo;
    @Mock
    private AndroidVersion androidVersion;

    private MerlinsBeard merlinsBeard;

    @Before
    public void setUp() {
        merlinsBeard = new MerlinsBeard(connectivityManager, androidVersion);
    }

    @Test
    public void givenNetworkIsDisconnected_whenCheckingForConnectivity_thenReturnsFalse() {
        given(connectivityManager.getActiveNetworkInfo()).willReturn(networkInfo);
        given(networkInfo.isConnected()).willReturn(DISCONNECTED);

        boolean connected = merlinsBeard.isConnected();

        assertThat(connected).isFalse();
    }

    @Test
    public void givenNetworkIsNull_whenCheckingForConnectivity_thenReturnsFalse() {
        given(connectivityManager.getActiveNetworkInfo()).willReturn(null);

        boolean connected = merlinsBeard.isConnected();

        assertThat(connected).isFalse();
    }

    @Test
    public void givenNetworkIsConnected_whenCheckingForConnectivity_thenReturnsTrue() {
        given(connectivityManager.getActiveNetworkInfo()).willReturn(networkInfo);
        given(networkInfo.isConnected()).willReturn(CONNECTED);

        boolean connected = merlinsBeard.isConnected();

        assertThat(connected).isTrue();
    }

    @Test
    public void givenNetworkIsConnectedViaWifi_andAndroidVersionIsBelowLollipop_whenCheckingIfConnectedToWifi_thenReturnsTrue() {
        givenNetworkStateForBelowLollipop(CONNECTED, ConnectivityManager.TYPE_WIFI);

        boolean connectedToWifi = merlinsBeard.isConnectedToWifi();

        assertThat(connectedToWifi).isTrue();
    }

    @Test
    public void givenNetworkIsDisconnected_andAndroidVersionIsBelowLollipop_whenCheckingIfConnectedToWifi_thenReturnsFalse() {
        givenNetworkStateForBelowLollipop(DISCONNECTED, ConnectivityManager.TYPE_WIFI);

        boolean connectedToWifi = merlinsBeard.isConnectedToWifi();

        assertThat(connectedToWifi).isFalse();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void givenNetworkIsConnectedViaWifi_andAndroidVersionIsLollipopOrAbove_whenCheckingIfConnectedToWifi_thenReturnsTrue() {
        givenNetworkStateForLollipopOrAbove(CONNECTED, ConnectivityManager.TYPE_WIFI);

        boolean connectedToWifi = merlinsBeard.isConnectedToWifi();

        assertThat(connectedToWifi).isTrue();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void givenNetworkIsDisconnected_andAndroidVersionIsLollipopOrAbove_whenCheckingIfConnectedToWifi_thenReturnsFalse() {
        givenNetworkStateForLollipopOrAbove(DISCONNECTED, ConnectivityManager.TYPE_WIFI);

        boolean connectedToWifi = merlinsBeard.isConnectedToWifi();

        assertThat(connectedToWifi).isFalse();
    }

    @Test
    public void givenNetworkConnectivityInfoIsUnavailable_andAndroidVersionIsBelowLollipop_whenCheckingIfConnectedToWifi_thenReturnsFalse() {
        given(androidVersion.isLollipopOrHigher()).willReturn(BELOW_LOLLIPOP);
        given(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).willReturn(null);

        boolean connectedToWifi = merlinsBeard.isConnectedToWifi();

        assertThat(connectedToWifi).isFalse();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void givenNetworkConnectivityInfoIsUnavailable_andAndroidVersionIsLollipopOrAbove_whenCheckingIfConnectedToWifi_thenReturnsFalse() {
        given(androidVersion.isLollipopOrHigher()).willReturn(LOLLIPOP_OR_ABOVE);
        given(connectivityManager.getAllNetworks()).willReturn(new Network[]{});

        boolean connectedToWifi = merlinsBeard.isConnectedToWifi();

        assertThat(connectedToWifi).isFalse();
    }

    @Test
    public void givenNetworkIsConnectedViaMobile_andAndroidVersionIsBelowLollipop_whenCheckingIfConnectedToMobile_thenReturnsTrue() {
        givenNetworkStateForBelowLollipop(CONNECTED, ConnectivityManager.TYPE_MOBILE);

        boolean connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork();

        assertThat(connectedToMobileNetwork).isTrue();
    }

    @Test
    public void givenNetworkIsDisconnected_andAndroidVersionIsBelowLollipop_whenCheckingIfConnectedToMobile_thenReturnsFalse() {
        givenNetworkStateForBelowLollipop(DISCONNECTED, ConnectivityManager.TYPE_WIFI);

        boolean connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork();

        assertThat(connectedToMobileNetwork).isFalse();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void givenNetworkIsConnectedViaMobile_andAndroidVersionIsLollipopOrAbove_whenCheckingIfConnectedToMobile_thenReturnsTrue() {
        givenNetworkStateForLollipopOrAbove(CONNECTED, ConnectivityManager.TYPE_MOBILE);

        boolean connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork();

        assertThat(connectedToMobileNetwork).isTrue();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void givenNetworkIsDisconnected_andAndroidVersionIsLollipopOrAbove_whenCheckingIfConnectedToMobile_thenReturnsFalse() {
        givenNetworkStateForLollipopOrAbove(DISCONNECTED, ConnectivityManager.TYPE_MOBILE);

        boolean connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork();

        assertThat(connectedToMobileNetwork).isFalse();
    }

    @Test
    public void givenNetworkConnectivityInfoIsUnavailable_andAndroidVersionIsLollipopOrAbove_whenCheckingIfConnectedToMobile_thenReturnsFalse() {
        given(androidVersion.isLollipopOrHigher()).willReturn(false);
        given(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).willReturn(null);

        boolean connectedToWifi = merlinsBeard.isConnectedToMobileNetwork();

        assertThat(connectedToWifi).isFalse();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void givenNoAvailableNetworks_andAndroidVersionIsLollipopOrAbove_whenCheckingIfConnectedToMobile_thenReturnsFalse() {
        given(androidVersion.isLollipopOrHigher()).willReturn(true);
        given(connectivityManager.getAllNetworks()).willReturn(new Network[]{});

        boolean connectedToWifi = merlinsBeard.isConnectedToMobileNetwork();

        assertThat(connectedToWifi).isFalse();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void givenNetworkWithoutNetworkInfo_andAndroidVersionIsLollipopOrAbove_whenCheckingIfConnectedToMobile_thenReturnsFalse() {
        givenNetworkStateWithoutNetworkInfoForLollipopOrAbove();

        boolean connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork();

        assertThat(connectedToMobileNetwork).isFalse();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void givenNetworkStateForLollipopOrAbove(boolean isConnected, int networkType) {
        Network network = Mockito.mock(Network.class);
        NetworkInfo networkInfo = Mockito.mock(NetworkInfo.class);
        given(networkInfo.getType()).willReturn(networkType);
        given(networkInfo.isConnected()).willReturn(isConnected);
        given(connectivityManager.getNetworkInfo(network)).willReturn(networkInfo);
        given(androidVersion.isLollipopOrHigher()).willReturn(LOLLIPOP_OR_ABOVE);
        given(connectivityManager.getAllNetworks()).willReturn(new Network[]{network});
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void givenNetworkStateWithoutNetworkInfoForLollipopOrAbove() {
        Network network = Mockito.mock(Network.class);
        given(androidVersion.isLollipopOrHigher()).willReturn(LOLLIPOP_OR_ABOVE);
        given(connectivityManager.getAllNetworks()).willReturn(new Network[]{network});
    }

    private void givenNetworkStateForBelowLollipop(boolean isConnected, int networkType) {
        NetworkInfo networkInfo = Mockito.mock(NetworkInfo.class);
        given(androidVersion.isLollipopOrHigher()).willReturn(BELOW_LOLLIPOP);
        given(connectivityManager.getNetworkInfo(networkType)).willReturn(networkInfo);
        given(networkInfo.isConnected()).willReturn(isConnected);
    }

}
