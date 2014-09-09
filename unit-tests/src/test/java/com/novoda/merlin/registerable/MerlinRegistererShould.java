package com.novoda.merlin.registerable;

import com.novoda.merlin.MerlinRobolectricTestRunner;
import com.novoda.merlin.registerable.connection.Connectable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MerlinRobolectricTestRunner.class)
public class MerlinRegistererShould {

    private MerlinRegisterer merlinRegisterer;

    @Before
    public void setUp() throws Exception {
        merlinRegisterer = new MerlinRegisterer();
    }

    @Test
    public void registerRegisterables() throws Exception {
        Registerable registerable = mock(Registerable.class);

        merlinRegisterer.register(registerable);

        assertThat(merlinRegisterer.get()).hasSize(1);
    }

    @Test
    public void notHoldReferences() throws Exception {
        Registerable registerable = mock(Registerable.class);

        merlinRegisterer.register(registerable);

        registerable = null;
        System.gc();

        assertThat(merlinRegisterer.get()).isEmpty();
    }

    @Test
    public void notRegisterTheSameObjectMoreThanOnce() throws Exception {
        Connectable connectable = mock(Connectable.class);

        merlinRegisterer.register(connectable);
        merlinRegisterer.register(connectable);
        merlinRegisterer.register(connectable);

        assertThat(merlinRegisterer.get()).hasSize(1);
    }

}
