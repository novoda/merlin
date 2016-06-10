package com.novoda.merlin.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static com.novoda.merlin.service.ResponseCodeValidator.DefaultEndpointResponseCodeValidator;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class ResponseCodeValidatorTests {

    @RunWith(Parameterized.class)
    public static class DefaultEndpointResponseCodeValidatorTest {

        private final int responseCode;
        private final boolean isValid;

        @Parameterized.Parameters(name = " response code {0} should return {1}")
        public static Collection<Object[]> data() {
            return ResponseCode.toParameterList();
        }

        public DefaultEndpointResponseCodeValidatorTest(int responseCode, boolean isValid) {
            this.responseCode = responseCode;
            this.isValid = isValid;
        }

        @Test
        public void testOtherCodesReturnFalse() {
            boolean actual = new DefaultEndpointResponseCodeValidator().isResponseCodeValid(responseCode);
            assertThat(actual).isEqualTo(isValid);
        }
    }

    enum ResponseCode {
        OK(200, false), CREATED(201, false), NO_CONTENT(204, true), MOVED_PERMANENTLY(301, false), NOT_FOUND(404, false), SERVER_ERROR(500, false);

        private final int code;
        private final boolean isValid;

        ResponseCode(int code, boolean isValid) {
            this.code = code;
            this.isValid = isValid;
        }

        public static Collection<Object[]> toParameterList() {
            List<Object[]> list = new ArrayList<>();
            for (ResponseCode responseCode : values()) {
                list.add(new Object[]{responseCode.code, responseCode.isValid});
            }
            return list;
        }
    }
}
