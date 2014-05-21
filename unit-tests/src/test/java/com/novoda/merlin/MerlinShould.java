package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.service.MerlinServiceBinder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class MerlinShould {

    @Mock private Context context;
    @Mock private MerlinServiceBinder serviceBinder;

    private Merlin merlin;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        merlin = new Merlin(serviceBinder, null);
    }

    @Test
    public void start_the_merlin_service() throws Exception {
        merlin.bind();

        verify(serviceBinder).bindService();
    }

    @Test
    public void bind_the_merlin_service_with_a_hostname_when_provided() throws Exception {
        String hostname = "start_the_merlin_service_with_a_hostname_when_provided";

        merlin.setHostname(hostname);
        merlin.bind();

        verify(serviceBinder).setHostname(hostname);
    }

    @Test
    public void unbind_the_merlin_service() throws Exception {
        merlin.unbind();

        verify(serviceBinder).unbind();
    }

}
