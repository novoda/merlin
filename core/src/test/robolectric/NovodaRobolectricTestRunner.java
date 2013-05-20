package robolectric;

import java.io.File;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricContext;
import org.robolectric.RobolectricTestRunner;

public class NovodaRobolectricTestRunner extends RobolectricTestRunner {

    public NovodaRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(RobolectricContext.bootstrap(NovodaRobolectricTestRunner.class, testClass, new RobolectricContext.Factory() {
            @Override
            public RobolectricContext create() {
                return new RobolectricContext() {
                    @Override
                    protected AndroidManifest createAppManifest() {
                        return new AndroidManifest(new File("test/resources/robolectric"));
                    }
                };
            }
        }));
    }

}