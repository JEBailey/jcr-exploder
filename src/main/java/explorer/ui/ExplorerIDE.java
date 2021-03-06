/*
Copyright 2016 JE Bailey

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package explorer.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.text.JTextComponent;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.ui.contentview.TabContainer;

@Component(name = "ExplorerIDE")
public class ExplorerIDE implements Runnable {

	private JFrame frmJcrExploder;

	private ComponentContext componentContext;

	private static final Logger log = LoggerFactory.getLogger(ExplorerIDE.class);

	@Reference
	private JTree jTree;
	
	@Reference
	private TabContainer editorTab;

	@Reference
	private JTable table;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJcrExploder = new JFrame();
		frmJcrExploder.setTitle("Sling Resource Explorer");
		frmJcrExploder.setBounds(100, 100, 800, 600);
		frmJcrExploder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(3);
		frmJcrExploder.getContentPane().add(splitPane, BorderLayout.CENTER);

		splitPane.setRightComponent(editorTab);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		scrollPane.setViewportView(jTree);

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		scrollPane.setColumnHeaderView(panel);

		JButton button = new JButton("+");
		button.setToolTipText("expand all");
		button.setContentAreaFilled(false);
		button.setBorder(new CompoundBorder());
		panel.add(button);
		splitPane.setDividerLocation(200);

		JMenuBar menuBar = new JMenuBar();
		frmJcrExploder.setJMenuBar(menuBar);
	}

	@Activate
	public void activate(ComponentContext context) throws Exception {
		this.componentContext = context;
		try {
			javax.swing.SwingUtilities.invokeAndWait(this);
		} catch (Exception ex) {
			log.error("non event dispatch thread errored");
			throw ex;
		}
	}

	@Deactivate
	public void deactivate() throws Exception {
		UIManager.put("RTextAreaUI.actionMap", null);
		UIManager.put("RSyntaxTextAreaUI.actionMap", null);
		JTextComponent.removeKeymap("RTextAreaKeymap");

		frmJcrExploder.setVisible(false);
		frmJcrExploder.dispose();
		frmJcrExploder = null;
	}

	@Override
	public void run() {
		initialize();
		frmJcrExploder.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmJcrExploder.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				componentContext.disableComponent("ExplorerIDE");
			}
		});
		frmJcrExploder.setVisible(true);
		log.info("application is running");
	}

}
