package com.novoda.merlin;

public class NetworkStatus {

    public enum State {
        AVAILABLE,
        UNAVAILABLE;
    }

    private final State state;

    public static NetworkStatus newAvailableInstance() {
        return new NetworkStatus(State.AVAILABLE);
    }

    public static NetworkStatus newUnavailableInstance() {
        return new NetworkStatus(State.UNAVAILABLE);
    }

    private NetworkStatus(State state) {
        this.state = state;
    }

    public boolean isAvailable() {
        return state.equals(State.AVAILABLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NetworkStatus that = (NetworkStatus) o;
        if (state != that.state) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return state != null ? state.hashCode() : 0;
    }

}
