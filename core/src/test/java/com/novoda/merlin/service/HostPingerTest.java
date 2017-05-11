package com.novoda.merlin.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class HostPingerTest {

    private static final String HOST_ADDRESS = "any host address";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private PingTask pingTask;
    @Mock
    private PingTaskFactory pingTaskFactory;
    @Mock
    private HostPinger.PingerCallback pingerCallback;

    private HostPinger hostPinger;

    @Before
    public void setUp() {
        given(pingTaskFactory.create(HOST_ADDRESS)).willReturn(pingTask);
        hostPinger = new HostPinger(pingerCallback, HOST_ADDRESS, pingTaskFactory);
    }

    @Test
    public void whenPinging_thenCreatesPingTask() {
        hostPinger.ping();

        verify(pingTaskFactory).create(HOST_ADDRESS);
    }

    @Test
    public void whenPinging_thenExecutesPingTask() {
        hostPinger.ping();

        verify(pingTask).execute();
    }

    @Test
    public void whenNoNetworkToPing_thenCallsOnFailure() {
        hostPinger.noNetworkToPing();

        verify(pingerCallback).onFailure();
    }

}
