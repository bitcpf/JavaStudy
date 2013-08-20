package test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import test.api.SampleService;
import test.test.impl.SampleServiceImpl;

/**
 * Activator
 *
 * @author Игорь Елькин (ielkin@sitronics.com)
 */
public class Activator implements BundleActivator {
    private ServiceRegistration serviceRegistration;

    public void start(final BundleContext context) throws Exception {
        serviceRegistration = context.registerService(
                SampleService.class.getName(), new SampleServiceImpl(), null);
    }

    public void stop(final BundleContext context) throws Exception {
        serviceRegistration.unregister();
    }
}
