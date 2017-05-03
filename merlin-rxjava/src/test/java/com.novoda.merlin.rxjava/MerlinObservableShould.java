package com.novoda.merlin.rxjava;

import android.content.Context;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import rx.Subscription;
import rx.observers.AssertableSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MerlinObservableShould {

    @Mock
    Merlin merlin;

    AssertableSubscriber<NetworkStatus.State> testSubscriber;

    @Before
    public void setUp() {
        initMocks(this);

        testSubscriber = MerlinObservable.from(merlin)
                                         .test();
    }

    @Test
    public void unbindWhenUnsubscribed() {
        Subscription subscription = MerlinObservable.from(merlin)
                                                    .subscribe();
        subscription.unsubscribe();

        verify(merlin).unbind();
    }

    @Test
    public void receiveOneAvailableNetworkStatusStateOnConnect() {
        ArgumentCaptor<Connectable> argumentCaptor = ArgumentCaptor.forClass(Connectable.class);

        verify(merlin).registerConnectable(argumentCaptor.capture());
        argumentCaptor.getValue()
                      .onConnect();

        testSubscriber.assertValue(NetworkStatus.State.AVAILABLE);
    }

    @Test
    public void receiveOneUnavailableNetworkStatusStateOnDisconnect() {
        ArgumentCaptor<Disconnectable> argumentCaptor = ArgumentCaptor.forClass(Disconnectable.class);

        verify(merlin).registerDisconnectable(argumentCaptor.capture());
        argumentCaptor.getValue()
                      .onDisconnect();

        testSubscriber.assertValue(NetworkStatus.State.UNAVAILABLE);
    }

    @Test
    public void receiveOneAvailableNetworkStatusStateOnBindWhenConnected() {
        ArgumentCaptor<Bindable> argumentCaptor = ArgumentCaptor.forClass(Bindable.class);

        verify(merlin).registerBindable(argumentCaptor.capture());
        argumentCaptor.getValue()
                      .onBind(NetworkStatus.newAvailableInstance());

        testSubscriber.assertValue(NetworkStatus.State.AVAILABLE);
    }

    @Test
    public void receiveOneUnavailableNetworkStatusStateOnBindWhenDisconnected() {
        ArgumentCaptor<Bindable> argumentCaptor = ArgumentCaptor.forClass(Bindable.class);

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
