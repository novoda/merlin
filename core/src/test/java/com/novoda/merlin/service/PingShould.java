package com.novoda.merlin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(JUnit4.class)
public class PingShould {

    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int NO_CONTENT = 204;
    public static final int MOVED_PERMANENTLY = 301;
    private static final String HOST_ADDRESS = "any host address";

    @Mock
    private HostPinger.PingerCallback mockPingerCallback;

    @Mock
    private HostPinger.ResponseCodeFetcher mockResponseCodeFetcher;

    private Ping ping;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        ping = new Ping(HOST_ADDRESS, mockResponseCodeFetcher);
    }

    @Test
    public void returnsTrueWhenHostResponseCodeIsSuccess() throws Exception {
        when(mockResponseCodeFetcher.from(HOST_ADDRESS)).thenReturn(OK);

        boolean isSuccess = ping.doSynchronousPing();

        assertThat(isSuccess).isTrue();
    }

    @Test
    public void returnsTrueWhenHostResponseCodeIsCreated() throws Exception {
        when(mockResponseCodeFetcher.from(HOST_ADDRESS)).thenReturn(CREATED);

        boolean isSuccess = ping.doSynchronousPing();

        assertThat(isSuccess).isTrue();
    }

    @Test
    public void returnsTrueWhenHostResponseCodeIsNoContent() throws Exception {
        when(mockResponseCodeFetcher.from(HOST_ADDRESS)).thenReturn(NO_CONTENT);

        boolean isSuccess = ping.doSynchronousPing();

        assertThat(isSuccess).isTrue();
    }

    @Test
    public void returnsFalseWhenHostResponseCodeIsMovedPermanently() throws Exception {
        when(mockResponseCodeFetcher.from(HOST_ADDRESS)).thenReturn(MOVED_PERMANENTLY);

        boolean isSuccess = ping.doSynchronousPing();

        assertThat(isSuccess).isFalse();
    }
}
