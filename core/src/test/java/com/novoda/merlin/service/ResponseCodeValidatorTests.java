package com.novoda.merlin.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static com.novoda.merlin.service.ResponseCodeValidator.CustomEndpointResponseCodeValidator;
import static com.novoda.merlin.service.ResponseCodeValidator.DefaultEndpointResponseCodeValidator;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class ResponseCodeValidatorTests {

    private static final int NO_CONTENT = 204;

    @RunWith(Parameterized.class)
    public static class DefaultEndpointResponseCodeValidatorTest {

        private final int responseCode;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Integer[]> data() {
            return ResponseCode.toParameterList();
        }

        public DefaultEndpointResponseCodeValidatorTest(int responseCode) {
            this.responseCode = responseCode;
        }

        @Test
        public void test204ReturnsTrue() {
            boolean actual = new DefaultEndpointResponseCodeValidator().isResponseCodeValid(NO_CONTENT);
            assertThat(actual).isTrue();
        }

        @Test
        public void testOtherCodesReturnFalse() {
            boolean actual = new DefaultEndpointResponseCodeValidator().isResponseCodeValid(responseCode);
            assertThat(actual).isFalse();
        }
    }

    @RunWith(Parameterized.class)
    public static class CustomEndpointResponseCodeValidatorTest {

        private final int responseCode;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Integer[]> data() {
            Collection<Integer[]> integers = ResponseCode.toParameterList();
            integers.add(new Integer[]{NO_CONTENT});
            return integers;
        }

        public CustomEndpointResponseCodeValidatorTest(int responseCode) {
            this.responseCode = responseCode;
        }

        @Test
        public void anyResponseCodeIsValid() {
            boolean actual = new CustomEndpointResponseCodeValidator().isResponseCodeValid(responseCode);
            assertThat(actual).isTrue();
        }
    }

    enum ResponseCode {
        OK(200), CREATED(201), MOVED_PERMANENTLY(301), NOT_FOUND(404), SERVER_ERROR(500);

        private final int code;

        ResponseCode(int code) {
            this.code = code;
        }

        public static Collection<Integer[]> toParameterList() {
            List<Integer[]> list = new ArrayList<>();
            for (ResponseCode responseCode : values()) {
                list.add(new Integer[]{responseCode.code});
            }
            return list;
        }
    }
}
