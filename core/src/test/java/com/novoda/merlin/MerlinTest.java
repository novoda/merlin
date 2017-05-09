package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.service.MerlinServiceBinder;
import com.novoda.merlin.service.ResponseCodeValidator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MerlinTest {

    @Mock
    private Context context;
    @Mock
    private MerlinServiceBinder serviceBinder;
    @Mock
    ResponseCodeValidator mockValidator;

    private Merlin merlin;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        merlin = new Merlin(serviceBinder, null);
    }

    @Test
    public void startTheMerlinService() throws Exception {
        merlin.bind();

        verify(serviceBinder).bindService();
    }

    @Test
    public void bindTheMerlinServiceWithAHostnameWhenProvided() throws Exception {
        String hostname = "startTheMerlinServiceWithAHostnameWhenProvided";

        merlin.setEndpoint(hostname, mockValidator);
        merlin.bind();

        verify(serviceBinder).setEndpoint(hostname, mockValidator);
    }

    @Test
    public void unbindTheMerlinService() throws Exception {
        merlin.unbind();

        verify(serviceBinder).unbind();
    }

}
