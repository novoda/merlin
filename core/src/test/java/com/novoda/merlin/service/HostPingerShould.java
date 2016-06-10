package com.novoda.merlin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(JUnit4.class)
public class HostPingerShould {

    private static final String HOST_ADDRESS = "any host address";

    private HostPinger hostPinger;

    @Mock
    private PingTask mockPingTask;
    @Mock
    private Ping mockPing;
    @Mock
    private PingTaskFactory mockPingTaskFactory;
    @Mock
    private PingFactory mockPingFactory;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(mockPingFactory.create(HOST_ADDRESS)).thenReturn(mockPing);
        when(mockPingTaskFactory.create(mockPing)).thenReturn(mockPingTask);
        hostPinger = new HostPinger(mock(HostPinger.PingerCallback.class), HOST_ADDRESS, mockPingFactory, mockPingTaskFactory);
    }

    @Test
    public void createsPingTaskWhenPing() {
        hostPinger.ping();

        verify(mockPingFactory).create(HOST_ADDRESS);
        verify(mockPingTaskFactory).create(mockPing);
    }

    @Test
    public void createsExecutesPingTaskWhenPing() {
        hostPinger.ping();

        verify(mockPingTask).execute();
    }

}
