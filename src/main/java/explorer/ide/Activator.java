package explorer.ide;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator, Runnable {

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	private JFrame frame;
	private BundleContext context;

	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;
		ServiceReference factory = context.getServiceReference(ResourceResolverFactory.class.getName());
		this.resourceResolverFactory = (ResourceResolverFactory) context.getService(factory);
		if (SwingUtilities.isEventDispatchThread()) {
			run();
		} else {
			try {
				javax.swing.SwingUtilities.invokeAndWait(this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		final JFrame m_frame = frame;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				m_frame.setVisible(false);
				m_frame.dispose();
			}
		});
	}

	@Override
	public void run() {
		ResourceResolver resourceResolver = null;// =
		try {
			if (resourceResolverFactory != null){
				resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
			} else {
				
			}
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		frame = new ExplorerIDE(resourceResolver).frmJcrExploder;
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				/*
				try {
					
					context.getBundle(0).stop();
				} catch (BundleException e) {
					e.printStackTrace();
				}*/
			}
		});
		frame.setVisible(true);
	}

}
