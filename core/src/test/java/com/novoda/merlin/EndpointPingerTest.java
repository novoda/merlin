package com.novoda.merlin;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EndpointPingerTest {

    private static final Endpoint ENDPOINT = Endpoint.from("any endpoint");

    private final PingTask pingTask = mock(PingTask.class);
    private final PingTaskFactory pingTaskFactory = mock(PingTaskFactory.class);
    private final EndpointPinger.PingerCallback pingerCallback = mock(EndpointPinger.PingerCallback.class);

    private EndpointPinger endpointPinger;

    @Before
    public void setUp() {
        given(pingTaskFactory.create(ENDPOINT, pingerCallback)).willReturn(pingTask);
        endpointPinger = new EndpointPinger(ENDPOINT, pingTaskFactory);
    }

    @Test
    public void whenPinging_thenCreatesPingTask() {
        endpointPinger.ping(pingerCallback);

        verify(pingTaskFactory).create(ENDPOINT, pingerCallback);
    }

    @Test
    public void whenPinging_thenExecutesPingTask() {
        endpointPinger.ping(pingerCallback);

        verify(pingTask).execute();
    }

    @Test
    public void whenNoNetworkToPing_thenCallsOnFailure() {
        endpointPinger.noNetworkToPing(pingerCallback);

        verify(pingerCallback).onFailure();
    }

}
