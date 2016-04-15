package com.novoda.merlin.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(Enclosed.class)
public class PingShould {

    private static final String HOST_ADDRESS = "any host address";

    @RunWith(Parameterized.class)
    public static class GivenSuccessfulRequest {

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Integer[]> data() {
            return ResponseCode.toParameterList();
        }

        private final Ping ping;

        @Mock
        private HostPinger.ResponseCodeFetcher mockResponseCodeFetcher;

        private final int responseCode;

        public GivenSuccessfulRequest(int responseCode) {
            initMocks(this);
            ping = new Ping(HOST_ADDRESS, mockResponseCodeFetcher);
            this.responseCode = responseCode;
        }

        @Test
        public void returnsTrue() {
            when(mockResponseCodeFetcher.from(HOST_ADDRESS)).thenReturn(responseCode);

            boolean isSuccess = ping.doSynchronousPing();

            assertThat(isSuccess).isTrue();
        }

        private enum ResponseCode {
            OK(200), CREATED(201), NO_CONTENT(204), MOVED_PERMANENTLY(301), NOT_FOUND(404), SERVER_ERROR(500);

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
}
