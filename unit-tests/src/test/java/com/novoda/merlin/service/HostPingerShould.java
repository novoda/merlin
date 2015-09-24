package com.novoda.merlin.service;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinRobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MerlinRobolectricTestRunner.class)
public class HostPingerShould {

    public static final int SUCCESS_CODE = 200;
    public static final int NON_SUCCESS_CODE = 201;

    @Mock
    private HostPinger.PingerCallback pingerCallback;
    @Mock
    private HostPinger.ResponseCodeFetcher responseCodeFetcher;

    private HostPinger hostPinger;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        hostPinger = new HostPinger(pingerCallback, responseCodeFetcher);
    }

    @Test
    public void callSuccessWhenHostResponseCodeIs200() throws Exception {
        when(responseCodeFetcher.from(anyString())).thenReturn(SUCCESS_CODE);

        hostPinger.ping();

        verify(pingerCallback).onSuccess();
    }

    @Test
    public void callFailureWhenHostResponseCodeIsNot200() throws Exception {
        when(responseCodeFetcher.from(anyString())).thenReturn(NON_SUCCESS_CODE);

        hostPinger.ping();

        verify(pingerCallback).onFailure();
    }

    @Test
    public void pingAgainstAProvidedHostname() throws Exception {
        String hostname = "pingAgainstAProvidedHostname";
        hostPinger.setEndpoint(hostname);

        hostPinger.ping();

        verify(responseCodeFetcher).from(hostname);
    }

    @Test
    public void useTheDefaultHostnameWhenNoHostnameIsSet() throws Exception {
        hostPinger.ping();

        verify(responseCodeFetcher).from(Merlin.DEFAULT_ENDPOINT);
    }
}
