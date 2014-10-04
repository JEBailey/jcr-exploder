package explorer.ui;

import javax.swing.UIManager;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	
	public static BundleContext context;
	

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		UIManager.setLookAndFeel(
	            UIManager.getCrossPlatformLookAndFeelClassName());
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}


}
