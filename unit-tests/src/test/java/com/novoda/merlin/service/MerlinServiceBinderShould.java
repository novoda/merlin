package com.novoda.merlin.service;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.novoda.merlin.MerlinRobolectricTestRunner;
import com.novoda.merlin.registerable.bind.BindListener;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MerlinRobolectricTestRunner.class)
public class MerlinServiceBinderShould {

    private MerlinServiceBinder merlinServiceBinder;

    @Mock
    private Context context;

    @Before
    public void setUp() {
        initMocks(this);
        merlinServiceBinder = new MerlinServiceBinder(context, mock(ConnectListener.class), mock(DisconnectListener.class), mock(BindListener.class));
    }

    @Test
    public void forwardToTheContextServiceBind() {
        merlinServiceBinder.bindService();

        verify(context).bindService(any(Intent.class), any(ServiceConnection.class), anyInt());
    }

}

