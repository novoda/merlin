package com.novoda.merlin.rxjava2;

import android.content.Context;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinBuilder;
import com.novoda.merlin.NetworkStatus;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class MerlinFlowable {

    public static Flowable<NetworkStatus.State> from(Context context) {
        MerlinBuilder merlinBuilder = new Merlin.Builder();
        merlinBuilder.withAllCallbacks();
        return from(context, merlinBuilder);
    }

    public static Flowable<NetworkStatus.State> from(Context context, MerlinBuilder merlinBuilder) {
        return from(merlinBuilder.build(context));
    }

    public static Flowable<NetworkStatus.State> from(Merlin merlin) {
        return createFlowable(merlin);
    }

    private static Flowable<NetworkStatus.State> createFlowable(Merlin merlin) {
        return Flowable.create(new MerlinFlowableOnSubscribe(merlin), BackpressureStrategy.BUFFER)
                       .distinctUntilChanged();
    }
}
