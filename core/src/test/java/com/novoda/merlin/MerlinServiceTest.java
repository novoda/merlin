package com.novoda.merlin;

import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class MerlinServiceTest {

    private static final ConnectivityChangeEvent ANY_CONNECTIVITY_CHANGE_EVENT = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
            true, "any_info", "any_reason"
    );

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final Intent intent = mock(Intent.class);
    private final ConnectivityChangesRegister connectivityChangesRegister = mock(ConnectivityChangesRegister.class);
    private final ConnectivityChangesForwarder connectivityChangesForwarder = mock(ConnectivityChangesForwarder.class);

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

        verify(connectivityChangesRegister).register(any(MerlinService.ConnectivityChangesNotifier.class));
    }

    @Test
    public void givenRegisteredMerlinService_whenConnectivityChangeOccurs_thenNotifiesForwarder() {
        MerlinService.ConnectivityChangesNotifier connectivityChangesNotifier = givenRegisteredMerlinService();

        connectivityChangesNotifier.notify(ANY_CONNECTIVITY_CHANGE_EVENT);

        verify(connectivityChangesForwarder).forward(ANY_CONNECTIVITY_CHANGE_EVENT);
    }

    @Test
    public void givenConnectivityChangesRegisterIsNotBound_whenBindCompletes_thenThrowsException_andStopsWorkOnService() {
        thrown.expect(MerlinServiceDependencyMissingExceptionMatcher.from(ConnectivityChangesRegister.class));

        MerlinService.LocalBinder binder = (MerlinService.LocalBinder) merlinService.onBind(intent);
        binder.setConnectivityChangesForwarder(connectivityChangesForwarder);

        binder.onBindComplete();

        verifyZeroInteractions(connectivityChangesRegister);
        verifyZeroInteractions(connectivityChangesForwarder);
    }

    @Test
    public void givenConnectivityChangesForwarderIsNotBound_whenBindCompletes_thenThrowsException_andStopsWorkOnService() {
        thrown.expect(MerlinServiceDependencyMissingExceptionMatcher.from(ConnectivityChangesForwarder.class));

        MerlinService.LocalBinder binder = (MerlinService.LocalBinder) merlinService.onBind(intent);
        binder.setConnectivityChangesRegister(connectivityChangesRegister);

        binder.onBindComplete();

        verifyZeroInteractions(connectivityChangesRegister);
        verifyZeroInteractions(connectivityChangesForwarder);
    }

    @Test
    public void givenUnboundService_whenNotifying_thenDoesNotForwardEvent() {
        MerlinService.LocalBinder localBinder = givenBoundMerlinService();

        merlinService.onUnbind(null);
        localBinder.notify(ANY_CONNECTIVITY_CHANGE_EVENT);

        verifyZeroInteractions(connectivityChangesForwarder);
    }

    @Test
    public void givenNullForwarder_whenNotifying_thenDoesNotForwardEvent() {
        MerlinService.LocalBinder localBinder = givenBoundMerlinService();
        localBinder.setConnectivityChangesForwarder(null);

        localBinder.notify(ANY_CONNECTIVITY_CHANGE_EVENT);

        verifyZeroInteractions(connectivityChangesForwarder);
    }

    private MerlinService.LocalBinder givenBoundMerlinService() {
        MerlinService.LocalBinder binder = ((MerlinService.LocalBinder) merlinService.onBind(intent));
        binder.setConnectivityChangesRegister(connectivityChangesRegister);
        binder.setConnectivityChangesForwarder(connectivityChangesForwarder);
        return binder;
    }

    private MerlinService.ConnectivityChangesNotifier givenRegisteredMerlinService() {
        MerlinService.LocalBinder localBinder = givenBoundMerlinService();
        localBinder.onBindComplete();
        ArgumentCaptor<MerlinService.ConnectivityChangesNotifier> argumentCaptor = ArgumentCaptor.forClass(MerlinService.ConnectivityChangesNotifier.class);
        verify(connectivityChangesRegister).register(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

}
