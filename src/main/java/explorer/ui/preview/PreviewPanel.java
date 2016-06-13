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
package explorer.ui.preview;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

@SuppressWarnings("serial")
@Component
@Service(value = PreviewPanel.class)
public class PreviewPanel extends JPanel {

	public PreviewPanel() {
		super(new BorderLayout());
	}
	
	JScrollPane propertiesPane;
	
	JScrollPane contentPane;
	
	@Reference
	JTable propertyTable;
	
	private static String checkmark =  	"\u2713 ";

	@Activate
	public void activate() {
		MouseAdapter mouse =  new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
			}
		};
		
		JButton propsButton = new JButton(checkmark+"Properties"){
			{
				setFocusable(false);
				setEnabled(false);
			}
		};
		
		
		JToolBar bar = new JToolBar();
		bar.add(Box.createHorizontalGlue());
		bar.add(propsButton);
		bar.add(new JButton(" File "));
		bar.setFloatable(false);

		propertiesPane = new JScrollPane(propertyTable);
		
		add("North", bar);
		add("Center", propertiesPane);
	}
	


}
