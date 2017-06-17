package com.novoda.merlin.registerable;

import com.novoda.merlin.registerable.connection.Connectable;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class RegisterTest {

    private Register register;

    @Before
    public void setUp() {
        register = new Register();
    }

    @Test
    public void givenRegisterable_whenRegistering_thenAddsRegisterableToList() {
        Registerable registerable = mock(Registerable.class);

        register.register(registerable);

        assertThat(register.get()).hasSize(1);
    }

    @Test
    public void givenRegisteredRegisterable_whenSystemPerformsGc_thenDoesNotHoldRegisterableReference() {
        Registerable registerable = givenRegisteredRegisterable();

        registerable = null;
        System.gc();

        assertThat(register.get()).isEmpty();
    }

    @Test
    public void givenConnectableRegisterable_whenRegisteringMultipleTimes_thenDoesNotRegisterTheSameObjectMoreThanOnce() {
        Connectable connectable = mock(Connectable.class);

        register.register(connectable);
        register.register(connectable);
        register.register(connectable);

        assertThat(register.get()).hasSize(1);
    }

    private Registerable givenRegisteredRegisterable() {
        Registerable registerable = mock(Registerable.class);
        register.register(registerable);
        return registerable;
    }

}
