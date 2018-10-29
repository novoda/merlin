package com.novoda.merlin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static com.google.common.truth.Truth.assertThat;
import static com.novoda.merlin.ResponseCodeValidator.CaptivePortalResponseCodeValidator;

public class ResponseCodeValidatorTest {

    @RunWith(Parameterized.class)
    public static class CaptivePortalResponseCodeValidatorTest {

        private final int responseCode;
        private final boolean isValid;

        @Parameterized.Parameters(name = "response code {0} should return {1}")
        public static Collection<Object[]> data() {
            return Responses.toParameterList();
        }

        public CaptivePortalResponseCodeValidatorTest(int responseCode, boolean isValid) {
            this.responseCode = responseCode;
            this.isValid = isValid;
        }

        @Test
        public void whenCheckingResponseCodeValidity() {
            boolean actual = new CaptivePortalResponseCodeValidator().isResponseCodeValid(responseCode);

            assertThat(actual).isEqualTo(isValid);
        }
    }

    private enum Responses {

        OK(200, false),
        CREATED(201, false),
        NO_CONTENT(204, true),
        MOVED_PERMANENTLY(301, false),
        NOT_FOUND(404, false),
        SERVER_ERROR(500, false);

        private final int code;
        private final boolean isValid;

        Responses(int code, boolean isValid) {
            this.code = code;
            this.isValid = isValid;
        }

        public static Collection<Object[]> toParameterList() {
            List<Object[]> list = new ArrayList<>();
            for (Responses responses : values()) {
                list.add(new Object[]{responses.code, responses.isValid});
            }
            return list;
        }
    }
}
