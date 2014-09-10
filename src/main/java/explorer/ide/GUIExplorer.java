package explorer.ide;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(description="Swing based explorer",label="Explorer IDE")
public class GUIExplorer implements Runnable {

	
	private static final Logger log = LoggerFactory
			.getLogger(Activator.class);
	
	
	private JFrame frame;
	
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
		UIManager.put("RTextAreaUI.actionMap", null);
		UIManager.put("RSyntaxTextAreaUI.actionMap", null);
		JTextComponent.removeKeymap("RTextAreaKeymap");
		
		frame.setVisible(false);
		frame.dispose();
		frame = null;
	}

	@Override
	public void run() {
		frame = new ExplorerIDE(Activator.context).frmJcrExploder;
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				//no idea right now
			}
		});
		frame.setVisible(true);
		log.info("application is running");
	}

}
