package com.novoda.merlin.service;

import com.novoda.merlin.service.request.RequestException;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.novoda.merlin.service.HostPinger.ResponseCodeFetcher;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PingTest {

    private static final String HOST_ADDRESS = "any host address";

    private static final int RESPONSE_CODE = 201;

    public static class GivenSuccessfulRequest {

        private Ping ping;

        @Mock
        private ResponseCodeFetcher mockResponseCodeFetcher;
        @Mock
        private ResponseCodeValidator mockResponseCodeValidator;

        @Before
        public void setUp() {
            initMocks(this);
            ping = new Ping(
                    HOST_ADDRESS,
                    mockResponseCodeFetcher,
                    mockResponseCodeValidator
            );
        }

        @Test
        public void responseCodeIsPassedToValidator() {
            when(mockResponseCodeFetcher.from(HOST_ADDRESS)).thenReturn(RESPONSE_CODE);

            ping.doSynchronousPing();

            verify(mockResponseCodeValidator).isResponseCodeValid(RESPONSE_CODE);
        }
    }

    public static class GivenFailingRequest {

        @Mock
        private ResponseCodeFetcher mockResponseCodeFetcher;
        @Mock
        private ResponseCodeValidator mockResponseCodeValidator;

        private Ping ping;

        @Before
        public void setUp() {
            initMocks(this);
            ping = new Ping(
                    HOST_ADDRESS,
                    mockResponseCodeFetcher,
                    mockResponseCodeValidator
            );
        }

        @Test
        public void returnsFalseIfFailureIsBecauseIO() {
            when(mockResponseCodeFetcher.from(HOST_ADDRESS)).thenThrow(new RequestException(new IOException()));

            boolean isSuccess = ping.doSynchronousPing();

            assertThat(isSuccess).isFalse();
        }

        @Test
        public void returnsFalseIfFailureIsNotBecauseIO() {
            when(mockResponseCodeFetcher.from(HOST_ADDRESS)).thenThrow(new RequestException(new RuntimeException()));

            boolean isSuccess = ping.doSynchronousPing();

            assertThat(isSuccess).isFalse();
        }
    }
}
