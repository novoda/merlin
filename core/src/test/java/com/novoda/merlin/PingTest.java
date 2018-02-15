package com.novoda.merlin;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.novoda.merlin.EndpointPinger.ResponseCodeFetcher;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PingTest {

    private static final Endpoint ENDPOINT = Endpoint.from("any endpoint");

    private static final int RESPONSE_CODE = 201;

    private final ResponseCodeFetcher responseCodeFetcher = mock(ResponseCodeFetcher.class);
    private final ResponseCodeValidator responseCodeValidator = mock(ResponseCodeValidator.class);

    private Ping ping;

    @Before
    public void setUp() {
        ping = new Ping(
                ENDPOINT,
                responseCodeFetcher,
                responseCodeValidator
        );
    }

    @Test
    public void givenSuccessfulRequest_whenSynchronouslyPinging_thenChecksResponseCodeIsValid() {
        given(responseCodeFetcher.from(ENDPOINT)).willReturn(RESPONSE_CODE);

        ping.doSynchronousPing();

        verify(responseCodeValidator).isResponseCodeValid(RESPONSE_CODE);
    }

    @Test
    public void givenSuccessfulRequest_whenSynchronouslyPinging_thenReturnsTrue() {
        given(responseCodeFetcher.from(ENDPOINT)).willReturn(RESPONSE_CODE);
        given(responseCodeValidator.isResponseCodeValid(RESPONSE_CODE)).willReturn(true);

        boolean isSuccess = ping.doSynchronousPing();

        assertThat(isSuccess).isTrue();
    }

    @Test
    public void givenRequestFailsWithIOException_whenSynchronouslyPinging_thenReturnsFalse() {
        given(responseCodeFetcher.from(ENDPOINT)).willThrow(new RequestException(new IOException()));

        boolean isSuccess = ping.doSynchronousPing();

        assertThat(isSuccess).isFalse();
    }

    @Test
    public void givenRequestFailsWithRuntimeException_whenSynchronouslyPinging_thenReturnsFalse() {
        given(responseCodeFetcher.from(ENDPOINT)).willThrow(new RequestException(new RuntimeException()));

        boolean isSuccess = ping.doSynchronousPing();

        assertThat(isSuccess).isFalse();
    }

}
