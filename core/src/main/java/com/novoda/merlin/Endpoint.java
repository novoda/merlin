package com.novoda.merlin;

public class Endpoint {

    private static final Endpoint DEFAULT_ENDPOINT = Endpoint.from("http://connectivitycheck.android.com/generate_204");

    private final String endpoint;

    public static Endpoint defaultEndpoint() {
        return DEFAULT_ENDPOINT;
    }

    public static Endpoint from(String endpoint) {
        return new Endpoint(endpoint);
    }

    private Endpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String asString() {
        return endpoint;
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
