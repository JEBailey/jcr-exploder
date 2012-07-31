package explorer.ide;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.ide.tree.JcrTreeCellRenderer;

@Component
public class Activator implements BundleActivator, Runnable {

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	
	private static final Logger log = LoggerFactory
			.getLogger(JcrTreeCellRenderer.class);
	
	
	private JFrame frame;
	
	private static BundleContext context;
	
	
	/*
	 * Method calls that are specifically handled 
	 * by the component runtime
	 */
	@Activate
	public void activate() throws Exception{
		try {
			javax.swing.SwingUtilities.invokeAndWait(this);
		} catch (Exception ex) {
			log.error("non event dispatch thread errored");
			throw ex;
		}
	}
	
	@Deactivate
	public void deactivate() throws Exception{
		frame.setVisible(false);
		frame.dispose();
		frame = null;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		UIManager.put("RTextAreaUI.actionMap", null);
		UIManager.put("RSyntaxTextAreaUI.actionMap", null);
		JTextComponent.removeKeymap("RTextAreaKeymap");
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
		log.info("application is running");
	}
	 

}
