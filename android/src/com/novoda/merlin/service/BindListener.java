package com.novoda.merlin.service;

import com.novoda.merlin.NetworkStatus;

public interface BindListener {
    void onMerlinBind(NetworkStatus networkStatus);
}
