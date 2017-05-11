package com.novoda.merlin.registerable;

import com.novoda.merlin.registerable.connection.Connectable;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MerlinRegistererTest {

    private MerlinRegisterer merlinRegisterer;

    @Before
    public void setUp() {
        merlinRegisterer = new MerlinRegisterer();
    }

    @Test
    public void givenRegisterable_whenRegistering_thenAddsRegisterableToList() {
        Registerable registerable = mock(Registerable.class);

        merlinRegisterer.register(registerable);

        assertThat(merlinRegisterer.get()).hasSize(1);
    }

    @Test
    public void givenRegisteredRegisterable_whenSystemPerformsGc_thenDoesNotHoldRegisterableReference() {
        Registerable registerable = givenRegisteredRegisterable();

        registerable = null;
        System.gc();

        assertThat(merlinRegisterer.get()).isEmpty();
    }

    @Test
    public void givenConnectableRegisterable_whenRegisteringMultipleTimes_thenDoesNotRegisterTheSameObjectMoreThanOnce() {
        Connectable connectable = mock(Connectable.class);

        merlinRegisterer.register(connectable);
        merlinRegisterer.register(connectable);
        merlinRegisterer.register(connectable);

        assertThat(merlinRegisterer.get()).hasSize(1);
    }

    private Registerable givenRegisteredRegisterable() {
        Registerable registerable = mock(Registerable.class);
        merlinRegisterer.register(registerable);
        return registerable;
    }

}
