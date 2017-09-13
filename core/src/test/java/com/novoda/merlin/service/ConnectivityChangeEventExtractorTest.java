package com.novoda.merlin.service;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.novoda.merlin.receiver.ConnectivityChangeEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ConnectivityChangeEventExtractorTest {

    private static final boolean CONNECTED = true;

    private static final String ANY_REASON = "reason";
    private static final String ANY_EXTRA_INFO = "extra info";

    private static final Network ANY_NETWORK = mock(Network.class);
    private static final NetworkInfo UNAVAILABLE_NETWORK_INFO = null;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ConnectivityManager connectivityManager;
    @Mock
    private NetworkInfo networkInfo;

    private ConnectivityChangeEventExtractor eventExtractor;

    @Before
    public void setUp() {
        given(networkInfo.isConnected()).willReturn(CONNECTED);
        given(networkInfo.getReason()).willReturn(ANY_REASON);
        given(networkInfo.getExtraInfo()).willReturn(ANY_EXTRA_INFO);

        eventExtractor = new ConnectivityChangeEventExtractor(connectivityManager);
    }

    @Test
    public void givenNetworkInfo_whenExtracting_thenReturnsConnectivityChangeEvent() {
        given(connectivityManager.getNetworkInfo(ANY_NETWORK)).willReturn(networkInfo);

        ConnectivityChangeEvent connectivityChangeEvent = eventExtractor.extractFrom(ANY_NETWORK);

        assertThat(connectivityChangeEvent).isEqualTo(ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
                CONNECTED,
                ANY_EXTRA_INFO,
                ANY_REASON
        ));
    }

    @Test
    public void givenNetworkInfoIsUnavailable_whenExtracting_thenReturnsConnectivityChangeEventWithoutConnection() {
        given(connectivityManager.getNetworkInfo(ANY_NETWORK)).willReturn(UNAVAILABLE_NETWORK_INFO);

        ConnectivityChangeEvent connectivityChangeEvent = eventExtractor.extractFrom(ANY_NETWORK);

        assertThat(connectivityChangeEvent).isEqualTo(ConnectivityChangeEvent.createWithoutConnection());
    }

}
