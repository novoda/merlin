package com.novoda.merlin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(JUnit4.class)
public class HostPingerShould {

    private static final String HOST_ADDRESS = "any host address";

    private HostPinger hostPinger;
    private PingTaskFactory mockPingTaskFactory;
    private PingTask mockPingTask;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockPingTask = mock(PingTask.class);
        mockPingTaskFactory = mock(PingTaskFactory.class);
        when(mockPingTaskFactory.create(HOST_ADDRESS)).thenReturn(mockPingTask);

        hostPinger = new HostPinger(null, HOST_ADDRESS, mockPingTaskFactory);
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
