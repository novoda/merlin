package com.novoda.merlin.rxjava2;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.TestSubscriber;

import java.util.List;

import org.junit.Test;

import org.mockito.ArgumentCaptor;

import static com.novoda.merlin.rxjava2.MerlinFlowable.from;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MerlinFlowableShould {

    @Test
    public void unbindWhenDisposed() {
        Merlin merlin = mock(Merlin.class);
        Disposable disposable = from(merlin).subscribe();
        disposable.dispose();

        verify(merlin, times(1)).unbind();
    }

    @Test
    public void receiveOneAvailableNetworkStatusStateOnConnect() {
        Merlin merlin = mock(Merlin.class);
        ArgumentCaptor<Connectable> argumentCaptor = ArgumentCaptor.forClass(Connectable.class);
        TestSubscriber<NetworkStatus.State> testSubscriber = MerlinFlowable.from(merlin)
                                                                           .test();

        verify(merlin).registerConnectable(argumentCaptor.capture());
        argumentCaptor.getValue()
                      .onConnect();
        List<NetworkStatus.State> stateList = testSubscriber.values();
        assertTrue(stateList.size() == 1);
        assertTrue(stateList.get(0) == NetworkStatus.State.AVAILABLE);
    }

    @Test
    public void receiveOneUnavailableNetworkStatusStateOnDisconnect() {
        Merlin merlin = mock(Merlin.class);
        ArgumentCaptor<Disconnectable> argumentCaptor = ArgumentCaptor.forClass(Disconnectable.class);
        TestSubscriber<NetworkStatus.State> testSubscriber = MerlinFlowable.from(merlin)
                                                                           .test();

        verify(merlin).registerDisconnectable(argumentCaptor.capture());
        argumentCaptor.getValue()
                      .onDisconnect();
        List<NetworkStatus.State> stateList = testSubscriber.values();
        assertTrue(stateList.size() == 1);
        assertTrue(stateList.get(0) == NetworkStatus.State.UNAVAILABLE);
    }

    @Test
    public void receiveOneAvailableNetworkStatusStateOnBindWhenConnected() {
        Merlin merlin = mock(Merlin.class);
        ArgumentCaptor<Bindable> argumentCaptor = ArgumentCaptor.forClass(Bindable.class);
        TestSubscriber<NetworkStatus.State> testSubscriber = MerlinFlowable.from(merlin)
                                                                           .test();

        verify(merlin).registerBindable(argumentCaptor.capture());
        argumentCaptor.getValue()
                      .onBind(NetworkStatus.newAvailableInstance());
        List<NetworkStatus.State> stateList = testSubscriber.values();
        assertTrue(stateList.size() == 1);
        assertTrue(stateList.get(0) == NetworkStatus.State.AVAILABLE);
    }

    @Test
    public void receiveOneUnavailableNetworkStatusStateOnBindWhenDisconnected() {
        Merlin merlin = mock(Merlin.class);
        ArgumentCaptor<Bindable> argumentCaptor = ArgumentCaptor.forClass(Bindable.class);
        TestSubscriber<NetworkStatus.State> testSubscriber = MerlinFlowable.from(merlin)
                                                                           .test();

        verify(merlin).registerBindable(argumentCaptor.capture());
        argumentCaptor.getValue()
                      .onBind(NetworkStatus.newUnavailableInstance());
        List<NetworkStatus.State> stateList = testSubscriber.values();
        assertTrue(stateList.size() == 1);
        assertTrue(stateList.get(0) == NetworkStatus.State.UNAVAILABLE);
    }
}
