package com.novoda.merlin.service;

import com.novoda.merlin.service.request.RequestException;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.novoda.merlin.service.HostPinger.ResponseCodeFetcher;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class PingTest {

    private static final String HOST_ADDRESS = "any host address";

    private static final int RESPONSE_CODE = 201;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ResponseCodeFetcher responseCodeFetcher;
    @Mock
    private ResponseCodeValidator responseCodeValidator;

    private Ping ping;

    @Before
    public void setUp() {
        ping = new Ping(
                HOST_ADDRESS,
                responseCodeFetcher,
                responseCodeValidator
        );
    }

    @Test
    public void givenSuccessfulRequest_whenSynchronouslyPinging_thenChecksResponseCodeIsValid() {
        given(responseCodeFetcher.from(HOST_ADDRESS)).willReturn(RESPONSE_CODE);

        ping.doSynchronousPing();

        verify(responseCodeValidator).isResponseCodeValid(RESPONSE_CODE);
    }

    @Test
    public void givenSuccessfulRequest_whenSynchronouslyPinging_thenReturnsTrue() {
        given(responseCodeFetcher.from(HOST_ADDRESS)).willReturn(RESPONSE_CODE);
        given(responseCodeValidator.isResponseCodeValid(RESPONSE_CODE)).willReturn(true);

        boolean isSuccess = ping.doSynchronousPing();

        assertThat(isSuccess).isTrue();
    }

    @Test
    public void givenRequestFailsWithIOException_whenSynchronouslyPinging_thenReturnsFalse() {
        given(responseCodeFetcher.from(HOST_ADDRESS)).willThrow(new RequestException(new IOException()));

        boolean isSuccess = ping.doSynchronousPing();

        assertThat(isSuccess).isFalse();
    }

    @Test
    public void givenRequestFailsWithRuntimeException_whenSynchronouslyPinging_thenReturnsFalse() {
        given(responseCodeFetcher.from(HOST_ADDRESS)).willThrow(new RequestException(new RuntimeException()));

        boolean isSuccess = ping.doSynchronousPing();

        assertThat(isSuccess).isFalse();
    }

}
