package com.novoda.merlin.service;

import com.novoda.merlin.service.request.RequestException;

import java.io.IOException;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.novoda.merlin.service.RequestExceptionHandler.CustomEndpointRequestExceptionHandler;
import static com.novoda.merlin.service.RequestExceptionHandler.DefaultEndpointRequestExceptionHandler;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class RequestExceptionHandlerTests {

    public static class DefaultEndpointRequestExceptionHandlerTest {
        @Test
        public void returnsFalseForAnyRequestException() {
            RequestException e = new RequestException(new Throwable("tests!"));
            boolean actual = new DefaultEndpointRequestExceptionHandler().handleRequestException(e);
            assertThat(actual).isFalse();
        }
    }

    public static class CustomEndpointRequestExceptionHandlerTest {
        @Test
        public void returnsFalseForIOException() {
            RequestException e = new RequestException(new IOException());
            boolean actual = new CustomEndpointRequestExceptionHandler().handleRequestException(e);
            assertThat(actual).isFalse();
        }

        @Test(expected = RequestException.class)
        public void propagatesExceptionIfNotBecauseIO() {
            RequestException e = new RequestException(new Throwable("tests!"));
            new CustomEndpointRequestExceptionHandler().handleRequestException(e);
        }
    }

}
