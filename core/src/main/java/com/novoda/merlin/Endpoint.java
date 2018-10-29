package com.novoda.merlin;

import java.net.MalformedURLException;
import java.net.URL;

public class Endpoint {

    private static final Endpoint CAPTIVE_PORTAL_ENDPOINT = Endpoint.from("https://connectivitycheck.android.com/generate_204");

    private final String endpoint;

    public static Endpoint captivePortalEndpoint() {
        return CAPTIVE_PORTAL_ENDPOINT;
    }

    public static Endpoint from(String endpoint) {
        return new Endpoint(endpoint);
    }

    private Endpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public URL asURL() throws MalformedURLException {
        return new URL(endpoint);
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "endpoint='" + endpoint + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Endpoint endpoint1 = (Endpoint) o;

        return endpoint != null ? endpoint.equals(endpoint1.endpoint) : endpoint1.endpoint == null;

    }

    @Override
    public int hashCode() {
        return endpoint != null ? endpoint.hashCode() : 0;
    }
}
