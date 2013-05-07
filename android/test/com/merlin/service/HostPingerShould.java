package com.merlin.service;

import com.merlin.Merlin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class HostPingerShould {

    public static final int SUCCESS_CODE = 200;
    public static final int NON_SUCCESS_CODE = 201;

    @Mock private HostPinger.PingerCallback pingerCallback;
    @Mock private HostPinger.ResponseCodeFetcher responseCodeFetcher;

    private HostPinger hostPinger;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        hostPinger = new HostPinger(pingerCallback, responseCodeFetcher);
    }

    @Test
    public void call_success_when_host_response_code_is_200() throws Exception {
        when(responseCodeFetcher.from(anyString())).thenReturn(SUCCESS_CODE);

        hostPinger.ping();

        verify(pingerCallback).onSuccess();
    }

    @Test
    public void call_failure_when_host_response_code_is_not_200() throws Exception {
        when(responseCodeFetcher.from(anyString())).thenReturn(NON_SUCCESS_CODE);

        hostPinger.ping();

        verify(pingerCallback).onFailure();
    }

    @Test
    public void ping_against_a_provided_hostname() throws Exception {
        String hostname = "ping_against_a_provided_hostname";
        hostPinger.setHostAddress(hostname);

        hostPinger.ping();

        verify(responseCodeFetcher).from(hostname);
    }

    @Test
    public void use_the_default_hostname_when_no_hostname_is_set() throws Exception {
        hostPinger.ping();

        verify(responseCodeFetcher).from(Merlin.DEFAULT_HOSTNAME);
    }

}
