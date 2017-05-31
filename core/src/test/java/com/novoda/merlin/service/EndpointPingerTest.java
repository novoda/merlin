package com.novoda.merlin.service;

import com.novoda.merlin.Endpoint;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class EndpointPingerTest {

    private static final Endpoint ENDPOINT = Endpoint.from("any endpoint");

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private PingTask pingTask;
    @Mock
    private PingTaskFactory pingTaskFactory;
    @Mock
    private EndpointPinger.PingerCallback pingerCallback;

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
