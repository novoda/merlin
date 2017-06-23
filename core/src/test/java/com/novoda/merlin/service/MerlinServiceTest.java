package com.novoda.merlin.service;

import android.content.Intent;

import com.novoda.merlin.receiver.ConnectivityChangeEvent;
import com.novoda.merlin.receiver.ConnectivityChangesRegister;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class MerlinServiceTest {

    private static final ConnectivityChangeEvent ANY_CONNECTIVITY_CHANGE_EVENT = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
            true, "any_info", "any_reason"
    );

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private Intent intent;
    @Mock
    private ConnectivityChangesRegister connectivityChangesRegister;
    @Mock
    private ConnectivityChangesForwarder connectivityChangesForwarder;

    private MerlinService merlinService;

    @Before
    public void setUp() {
        merlinService = new MerlinService();
    }

    @Test
    public void givenOnBindHasBeenCalled_whenCheckingIsBound_thenReturnsTrue() {
        givenBoundMerlinService();

        boolean bound = MerlinService.isBound();

        assertThat(bound).isTrue();
    }

    @Test
    public void givenBoundMerlinService_whenCallingOnUnbind_thenReturnsFalseForIsBound() {
        givenBoundMerlinService();

        merlinService.onUnbind(intent);

        assertThat(MerlinService.isBound()).isFalse();
    }

    @Test
    public void givenBoundMerlinService_whenCallingOnUnbind_thenUnregistersConnectivityChangesRegister() {
        givenBoundMerlinService();

        merlinService.onUnbind(intent);

        verify(connectivityChangesRegister).unregister();
    }

    @Test
    public void givenBoundMerlinService_whenBindCompletes_thenNotifiesOfInitialNetworkStatusUsingForwarder() {
        MerlinService.LocalBinder localBinder = givenBoundMerlinService();

        localBinder.onBindComplete();

        verify(connectivityChangesForwarder).forwardInitialNetworkStatus();
    }

    @Test
    public void givenBoundMerlinService_whenBindCompletes_thenRegistersForConnectivityChanges() {
        MerlinService.LocalBinder localBinder = givenBoundMerlinService();

        localBinder.onBindComplete();

        verify(connectivityChangesRegister).register(any(MerlinService.ConnectivityChangesListener.class));
    }

    @Test
    public void givenRegisteredMerlinService_whenConnectivityChangeOccurs_thenNotifiesForwarder() {
        MerlinService.ConnectivityChangesListener connectivityChangesListener = givenRegisteredMerlinService();

        connectivityChangesListener.onConnectivityChanged(ANY_CONNECTIVITY_CHANGE_EVENT);

        verify(connectivityChangesForwarder).forward(ANY_CONNECTIVITY_CHANGE_EVENT);
    }

    @Test
    public void givenConnectivityChangesRegisterIsNotBound_whenBindCompletes_thenThrowsException() {
        thrown.expect(LocalBinderDependencyMissingExceptionMatcher.from(ConnectivityChangesRegister.class));

        MerlinService.LocalBinder binder = (MerlinService.LocalBinder) merlinService.onBind(intent);
        binder.setConnectivityChangesForwarder(connectivityChangesForwarder);

        binder.onBindComplete();
    }

    @Test
    public void givenConnectivityChangesForwarderIsNotBound_whenBindCompletes_thenThrowsException() {
        thrown.expect(LocalBinderDependencyMissingExceptionMatcher.from(ConnectivityChangesForwarder.class));

        MerlinService.LocalBinder binder = (MerlinService.LocalBinder) merlinService.onBind(intent);
        binder.setConnectivityChangesRegister(connectivityChangesRegister);

        binder.onBindComplete();
    }

    private MerlinService.LocalBinder givenBoundMerlinService() {
        MerlinService.LocalBinder binder = ((MerlinService.LocalBinder) merlinService.onBind(intent));
        binder.setConnectivityChangesRegister(connectivityChangesRegister);
        binder.setConnectivityChangesForwarder(connectivityChangesForwarder);
        return binder;
    }

    private MerlinService.ConnectivityChangesListener givenRegisteredMerlinService() {
        MerlinService.LocalBinder localBinder = givenBoundMerlinService();
        localBinder.onBindComplete();
        ArgumentCaptor<MerlinService.ConnectivityChangesListener> argumentCaptor = ArgumentCaptor.forClass(MerlinService.ConnectivityChangesListener.class);
        verify(connectivityChangesRegister).register(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

}
