package com.novoda.merlin.rxjava;

import android.content.Context;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import org.junit.Test;

import org.mockito.ArgumentCaptor;

import rx.Subscription;
import rx.observers.AssertableSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MerlinObservableShould {

    @Test
    public void unbindWhenUnsubscribed() {
        Merlin merlin = mock(Merlin.class);
        Subscription subscription = MerlinObservable.from(merlin)
                                                    .subscribe();
        subscription.unsubscribe();

        verify(merlin).unbind();
    }

    @Test
    public void receiveOneAvailableNetworkStatusStateOnConnect() {
        Merlin merlin = mock(Merlin.class);
        ArgumentCaptor<Connectable> argumentCaptor = ArgumentCaptor.forClass(Connectable.class);
        AssertableSubscriber<NetworkStatus.State> testSubscriber = MerlinObservable.from(merlin)
                                                                                   .test();

        verify(merlin).registerConnectable(argumentCaptor.capture());
        argumentCaptor.getValue()
                      .onConnect();

        testSubscriber.assertValue(NetworkStatus.State.AVAILABLE);
    }

    @Test
    public void receiveOneUnavailableNetworkStatusStateOnDisconnect() {
        Merlin merlin = mock(Merlin.class);
        ArgumentCaptor<Disconnectable> argumentCaptor = ArgumentCaptor.forClass(Disconnectable.class);
        AssertableSubscriber<NetworkStatus.State > testSubscriber = MerlinObservable.from(merlin)
                                                                                    .test();

        verify(merlin).registerDisconnectable(argumentCaptor.capture());
        argumentCaptor.getValue()
                      .onDisconnect();

        testSubscriber.assertValue(NetworkStatus.State.UNAVAILABLE);
    }

    @Test
    public void receiveOneAvailableNetworkStatusStateOnBindWhenConnected() {
        Merlin merlin = mock(Merlin.class);
        ArgumentCaptor<Bindable> argumentCaptor = ArgumentCaptor.forClass(Bindable.class);
        AssertableSubscriber<NetworkStatus.State> testSubscriber = MerlinObservable.from(merlin)
                                                                                   .test();

        verify(merlin).registerBindable(argumentCaptor.capture());
        argumentCaptor.getValue()
                      .onBind(NetworkStatus.newAvailableInstance());

        testSubscriber.assertValue(NetworkStatus.State.AVAILABLE);
    }

    @Test
    public void receiveOneUnavailableNetworkStatusStateOnBindWhenDisconnected() {
        Merlin merlin = mock(Merlin.class);
        ArgumentCaptor<Bindable> argumentCaptor = ArgumentCaptor.forClass(Bindable.class);
        AssertableSubscriber<NetworkStatus.State> testSubscriber = MerlinObservable.from(merlin)
                                                                                   .test();

        verify(merlin).registerBindable(argumentCaptor.capture());
        argumentCaptor.getValue()
                      .onBind(NetworkStatus.newUnavailableInstance());

        testSubscriber.assertValue(NetworkStatus.State.UNAVAILABLE);
    }

    @Test
    public void notCrashWhenCreatingWithAMerlinBuilderWithoutCallbacks() {
        Context context = mock(Context.class);
        Merlin.Builder merlinBuilder = new Merlin.Builder();
        MerlinObservable.from(context, merlinBuilder).subscribe();
    }
}
