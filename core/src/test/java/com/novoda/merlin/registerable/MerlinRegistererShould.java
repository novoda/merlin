package main.java.demo.com.novoda.merlin.registerable;

import main.java.demo.com.novoda.merlin.registerable.connection.Connectable;
import robolectric.NovodaRobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(NovodaRobolectricTestRunner.class)
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

    @Test
    public void not_register_the_same_object_more_than_once() throws Exception {
        Connectable connectable = mock(Connectable.class);

        merlinRegisterer.register(connectable);
        merlinRegisterer.register(connectable);
        merlinRegisterer.register(connectable);

        assertThat(merlinRegisterer.get()).hasSize(1);
    }

}
