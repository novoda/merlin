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
    private ConnectivityReceiver.MerlinServiceRetriever merlinServiceRetriever;
    @Mock
    private MerlinService merlinService;
    @Mock
    private MerlinsBeard merlinsBeard;

    private ConnectivityChangeNotifier notifier;

    @Before
    public void setUp() {
        ConnectivityReceiver.MerlinsBeardRetriever merlinsBeardRetriever = mock(ConnectivityReceiver.MerlinsBeardRetriever.class);
        given(merlinServiceRetriever.getService(context)).willReturn(merlinService);
        given(merlinsBeardRetriever.getMerlinsBeard(context)).willReturn(merlinsBeard);

        notifier = new ConnectivityChangeNotifier(merlinsBeardRetriever, merlinServiceRetriever, eventCreator);

    }

    @Test
    public void givenNullIntent_whenNotifying_thenNeverCallsMerlinServiceOnConnectivityChanged() {
        Intent intent = null;

        notifier.notify(context, intent);

        verify(merlinService, never()).onConnectivityChanged(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void givenIntentWithoutConnectivityAction_whenNotifying_thenNeverCallsMerlinServiceOnConnectivityChanged() {
        Intent intent = givenIntentWithoutConnectivityAction();

        notifier.notify(context, intent);

        verify(merlinService, never()).onConnectivityChanged(any(ConnectivityChangeEvent.class));
    }

    @Test
    public void givenIntentWithConnectivityAction_whenNotifying_thenCallsMerlinServiceOnConnectivityChanged() {
        Intent intent = givenIntentWithConnectivityAction();

        notifier.notify(context, intent);

        verify(merlinService).onConnectivityChanged(ANY_CHANGE_EVENT);
    }

    @Test
    public void givenIntentWithConnectivityAction_butMerlinServiceIsNotPresent_whenNotifying_thenNeverCallsMerlinServiceOnConnectivityChanged() {
        Intent intent = givenIntentWithConnectivityAction();
        given(merlinServiceRetriever.getService(context)).willReturn(null);

        notifier.notify(context, intent);

        verify(merlinService, never()).onConnectivityChanged(any(ConnectivityChangeEvent.class));
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
}
