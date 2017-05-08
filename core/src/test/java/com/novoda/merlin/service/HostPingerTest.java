package com.novoda.merlin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(JUnit4.class)
public class HostPingerTest {

    private static final String HOST_ADDRESS = "any host address";

    private HostPinger hostPinger;

    @Mock
    private PingTask mockPingTask;
    @Mock
    private PingTaskFactory mockPingTaskFactory;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(mockPingTaskFactory.create(HOST_ADDRESS)).thenReturn(mockPingTask);
        hostPinger = new HostPinger(mock(HostPinger.PingerCallback.class), HOST_ADDRESS, mockPingTaskFactory);
    }

    @Test
    public void createsPingTaskWhenPing() {
        hostPinger.ping();

        verify(mockPingTaskFactory).create(HOST_ADDRESS);
    }

    @Test
    public void createsExecutesPingTaskWhenPing() {
        hostPinger.ping();

        verify(mockPingTask).execute();
    }

}
