package explorer.ide;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

@Component
public class Activator implements BundleActivator {
	
	public static BundleContext context;
	
	
	/*
	 * Method calls that are specifically handled 
	 * by the component runtime
	 */
	@Activate
	public void activate() throws Exception{

	}
	
	@Deactivate
	public void deactivate() throws Exception{

	}

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}


}
