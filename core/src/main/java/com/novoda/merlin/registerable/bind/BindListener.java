package com.novoda.merlin.registerable.bind;

import com.novoda.merlin.NetworkStatus;

public interface BindListener {
    void onMerlinBind(NetworkStatus networkStatus);
}
