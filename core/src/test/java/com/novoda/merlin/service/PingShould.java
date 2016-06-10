package com.novoda.merlin.service;

import com.novoda.merlin.service.request.RequestException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static com.novoda.merlin.service.HostPinger.ResponseCodeFetcher;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(Enclosed.class)
public class PingShould {

    private static final String HOST_ADDRESS = "any host address";

    public static class GivenSuccessfulRequest {

        private Ping ping;

        @Mock
        private ResponseCodeFetcher mockResponseCodeFetcher;
        @Mock
        private ResponseCodeValidator mockResponseCodeValidator;
        @Mock
        private RequestExceptionHandler mockRequestExceptionHandler;

        private final int responseCode = 234;

        @Before
        public void setup() {
            initMocks(this);
            ping = new Ping(
                    HOST_ADDRESS,
                    mockResponseCodeFetcher,
                    mockResponseCodeValidator,
                    mockRequestExceptionHandler
            );
        }

        @Test
        public void responseCodeIsPassedToValidator() {
            when(mockResponseCodeFetcher.from(HOST_ADDRESS)).thenReturn(responseCode);

            ping.doSynchronousPing();

            verify(mockResponseCodeValidator).isResponseCodeValid(responseCode);
        }
    }

    public static class GivenFailingRequest {

        @Mock
        private ResponseCodeFetcher mockResponseCodeFetcher;
        @Mock
        private ResponseCodeValidator mockResponseCodeValidator;
        @Mock
        private RequestExceptionHandler mockRequestExceptionHandler;

        private Ping ping;

        @Before
        public void setUp() {
            initMocks(this);
            ping = new Ping(
                    HOST_ADDRESS,
                    mockResponseCodeFetcher,
                    mockResponseCodeValidator,
                    mockRequestExceptionHandler
            );
        }

        @Test
        public void exceptionIsPassedToHandler() {
            RequestException expected = new RequestException(new RuntimeException());
            when(mockResponseCodeFetcher.from(HOST_ADDRESS)).thenThrow(expected);

            ping.doSynchronousPing();

            verify(mockRequestExceptionHandler).handleRequestException(expected);
        }
    }
}
