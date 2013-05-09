package com.novoda.merlin.registerable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class MerlinRegistererShould {

    private MerlinRegisterer merlinRegisterer;

    @Before
    public void setUp() throws Exception {
        merlinRegisterer = new MerlinRegisterer();
    }

    @Test
    public void register_registerables() throws Exception {
        Registerable registerable = mock(Registerable.class);

        merlinRegisterer.register(registerable);

        assertThat(merlinRegisterer.get()).hasSize(1);
    }

    @Test
    public void not_hold_references() throws Exception {
        Registerable registerable = mock(Registerable.class);

        merlinRegisterer.register(registerable);

        registerable = null;
        System.gc();

        assertThat(merlinRegisterer.get()).isEmpty();
    }

}
