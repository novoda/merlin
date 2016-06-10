package com.novoda.merlin.service;

class PingTaskFactory {

    private final HostPinger.PingerCallback pingerCallback;

    PingTaskFactory(HostPinger.PingerCallback pingerCallback) {
        this.pingerCallback = pingerCallback;
    }

    public PingTask create(Ping ping) {
        return new PingTask(ping, pingerCallback);
    }

}
