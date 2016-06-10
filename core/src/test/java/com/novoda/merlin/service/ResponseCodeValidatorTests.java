package com.novoda.merlin.service;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.novoda.merlin.service.ResponseCodeValidator.CustomEndpointResponseCodeValidator;
import static com.novoda.merlin.service.ResponseCodeValidator.DefaultEndpointResponseCodeValidator;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class ResponseCodeValidatorTests {
    public static class DefaultEndpointResponseCodeValidatorTest {

        @Test
        public void test204ReturnsTrue() {
            boolean actual = new DefaultEndpointResponseCodeValidator().isResponseCodeValid(204);
            assertThat(actual).isTrue();
        }

        @Test
        public void test200ReturnsFalse() {
            boolean actual = new DefaultEndpointResponseCodeValidator().isResponseCodeValid(200);
            assertThat(actual).isFalse();
        }
    }

    public static class CustomEndpointResponseCodeValidatorTest {
        @Test
        public void anyResponseCodeIsValid() {
            boolean actual = new CustomEndpointResponseCodeValidator().isResponseCodeValid(200);
            assertThat(actual).isTrue();
        }
    }
}
