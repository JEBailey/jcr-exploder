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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.ide.tree.JcrTreeCellRenderer;

public class Activator implements BundleActivator, Runnable {

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	private static final Logger log = LoggerFactory
			.getLogger(JcrTreeCellRenderer.class);
	
	private JFrame frame;
	private BundleContext context;

	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;
		ServiceReference factory = context.getServiceReference(ResourceResolverFactory.class.getName());
		if (factory == null){
			log.error("unable to obtain service reference factory");
		}
		this.resourceResolverFactory = (ResourceResolverFactory) context.getService(factory);
		if (SwingUtilities.isEventDispatchThread()) {
			run();
		} else {
			try {
				javax.swing.SwingUtilities.invokeAndWait(this);
			} catch (Exception ex) {
				log.error("non event dispatch thread errored");
				throw ex;
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
				frame = null;
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
				log.error("attempting to run with a null resource resolver factory");
			}
		} catch (Throwable e1) {
			log.error(e1.getLocalizedMessage());
		}
		frame = new ExplorerIDE(resourceResolver).frmJcrExploder;
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				try {
					//shutdown current bundle
					context.getBundle().stop();
				} catch (BundleException e) {
					log.error("problem shutting down");
				}
			}
		});
		frame.setVisible(true);
	}

}
