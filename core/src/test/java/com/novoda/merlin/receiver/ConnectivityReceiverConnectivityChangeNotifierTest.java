package com.novoda.merlin.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

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

public class ConnectivityReceiverConnectivityChangeNotifierTest {

    private static final ConnectivityChangeEvent ANY_CHANGE_EVENT = ConnectivityChangeEvent.createWithNetworkInfoChangeEvent(
            true, "any_info", "any_reason"
    );
    private static final boolean CAN_NOTIFY = true;
    private static final boolean CANNOT_NOTIFY = false;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Context context;
    @Mock
    private ConnectivityChangeEventCreator eventCreator;
    @Mock
    private ConnectivityReceiver.MerlinBinderRetriever merlinBinderRetriever;
    @Mock
    private MerlinsBeard merlinsBeard;
    @Mock
    private MerlinService.ConnectivityChangesNotifier connectivityChangesNotifier;

    private ConnectivityReceiverConnectivityChangeNotifier notifier;

    @Before
    public void setUp() {
        ConnectivityReceiver.MerlinsBeardCreator merlinsBeardCreator = mock(ConnectivityReceiver.MerlinsBeardCreator.class);

        given(connectivityChangesNotifier.canNotify()).willReturn(CAN_NOTIFY);

        given(merlinBinderRetriever.retrieveConnectivityChangesNotifier(context)).willReturn(connectivityChangesNotifier);
        given(merlinsBeardCreator.createMerlinsBeard(context)).willReturn(merlinsBeard);

        notifier = new ConnectivityReceiverConnectivityChangeNotifier(merlinsBeardCreator, merlinBinderRetriever, eventCreator);

    }

    @Test
    public void givenNullIntent_whenNotifying_thenNeverNotifiesOfConnectivityChangeEvent() {
        Intent intent = null;

        notifier.notify(context, intent);

        verify(connectivityChangesNotifier, never()).notify(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void givenIntentWithoutConnectivityAction_whenNotifying_thenNeverNotifiesOfConnectivityChangeEvent() {
        Intent intent = givenIntentWithoutConnectivityAction();

        notifier.notify(context, intent);

        verify(connectivityChangesNotifier, never()).notify(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void givenIntentWithConnectivityAction_whenNotifying_thenNotifiesOfConnectivityChangeEvent() {
        Intent intent = givenIntentWithConnectivityAction();

        notifier.notify(context, intent);

        verify(connectivityChangesNotifier).notify(ANY_CHANGE_EVENT);
    }

    @Test
    public void givenIntentWithConnectivityAction_butNullBinder_whenNotifying_thenNeverNotifiesOfConnectivityChangeEvent() {
        Intent intent = givenIntentWithConnectivityAction();
        given(merlinBinderRetriever.retrieveConnectivityChangesNotifier(context)).willReturn(null);

        notifier.notify(context, intent);

        verify(connectivityChangesNotifier, never()).notify(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void givenIntentWithConnectivityAction_butIncorrectBinder_whenNotifying_thenNeverNotifiesOfConnectivityChangeEvent() {
        Intent intent = givenIntentWithConnectivityAction();
        given(merlinBinderRetriever.retrieveConnectivityChangesNotifier(context)).willReturn(incorrectBinder());

        notifier.notify(context, intent);

        verify(connectivityChangesNotifier, never()).notify(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void givenIntentWithConnectivityAction_butCannotNotify_whenNotifying_thenNeverNotifiesOfConnectivityChangeEvent() {
        Intent intent = givenIntentWithConnectivityAction();
        given(connectivityChangesNotifier.canNotify()).willReturn(CANNOT_NOTIFY);

        notifier.notify(context, intent);

        verify(connectivityChangesNotifier, never()).notify(any(ConnectivityChangeEvent.class));
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

    private MerlinService.ConnectivityChangesNotifier incorrectBinder() {
        return null;
    }
}
