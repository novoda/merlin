package com.novoda.merlin.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;

import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.service.MerlinService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ConnectivityChangeNotifierTest {

    private static final ConnectivityChangeEvent ANY_CHANGE_EVENT = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
            true, "any_info", "any_reason"
    );

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Context context;
    @Mock
    private ConnectivityChangeEventCreator eventCreator;
    @Mock
    private ConnectivityReceiver.MerlinBinderRetriever merlinBinderRetriever;
    @Mock
    private MerlinService.ConnectivityChangesListener connectivityChangesListener;
    @Mock
    private MerlinsBeard merlinsBeard;
    @Mock
    private MerlinService.LocalBinder merlinBinder;

    private ConnectivityChangeNotifier notifier;

    @Before
    public void setUp() {
        ConnectivityReceiver.MerlinsBeardCreator merlinsBeardCreator = mock(ConnectivityReceiver.MerlinsBeardCreator.class);

        given(merlinBinder.connectivityChangesListener()).willReturn(connectivityChangesListener);

        given(merlinBinderRetriever.retrieveMerlinLocalServiceBinderIfAvailable(context)).willReturn(merlinBinder);
        given(merlinsBeardCreator.createMerlinsBeard(context)).willReturn(merlinsBeard);

        notifier = new ConnectivityChangeNotifier(merlinsBeardCreator, merlinBinderRetriever, eventCreator);

    }

    @Test
    public void givenNullIntent_whenNotifying_thenNeverCallsMerlinServiceOnConnectivityChanged() {
        Intent intent = null;

        notifier.notify(context, intent);

        verify(connectivityChangesListener, never()).onConnectivityChanged(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void givenIntentWithoutConnectivityAction_whenNotifying_thenNeverCallsMerlinServiceOnConnectivityChanged() {
        Intent intent = givenIntentWithoutConnectivityAction();

        notifier.notify(context, intent);

        verify(connectivityChangesListener, never()).onConnectivityChanged(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void givenIntentWithConnectivityAction_whenNotifying_thenCallsMerlinServiceOnConnectivityChanged() {
        Intent intent = givenIntentWithConnectivityAction();

        notifier.notify(context, intent);

        verify(connectivityChangesListener).onConnectivityChanged(ANY_CHANGE_EVENT);
    }

    @Test
    public void givenIntentWithConnectivityAction_butNullBinder_whenNotifying_thenNeverCallsMerlinServiceOnConnectivityChanged() {
        Intent intent = givenIntentWithConnectivityAction();
        given(merlinBinderRetriever.retrieveMerlinLocalServiceBinderIfAvailable(context)).willReturn(null);

        notifier.notify(context, intent);

        verify(connectivityChangesListener, never()).onConnectivityChanged(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void givenIntentWithConnectivityAction_butIncorrectBinder_whenNotifying_thenNeverCallsMerlinServiceOnConnectivityChanged() {
        Intent intent = givenIntentWithConnectivityAction();
        given(merlinBinderRetriever.retrieveMerlinLocalServiceBinderIfAvailable(context)).willReturn(incorrectBinder());

        notifier.notify(context, intent);

        verify(connectivityChangesListener, never()).onConnectivityChanged(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void givenIntentWithConnectivityAction_butBinderWithNullService_whenNotifying_thenNeverCallsMerlinServiceOnConnectivityChanged() {
        Intent intent = givenIntentWithConnectivityAction();
        given(merlinBinder.connectivityChangesListener()).willReturn(null);

        notifier.notify(context, intent);

        verify(connectivityChangesListener, never()).onConnectivityChanged(any(ConnectivityChangeEvent.class));
    }

    private Intent givenIntentWithoutConnectivityAction() {
        return mock(Intent.class);
    }

    private Intent givenIntentWithConnectivityAction() {
        Intent intent = mock(Intent.class);
        given(intent.getAction()).willReturn(ConnectivityManager.CONNECTIVITY_ACTION);
        given(eventCreator.createFrom(any(Intent.class), eq(merlinsBeard))).willReturn(ANY_CHANGE_EVENT);
        return intent;
    }

    private IBinder incorrectBinder() {
        return new Binder();
    }
}
