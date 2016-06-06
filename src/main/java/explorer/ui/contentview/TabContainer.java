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
package explorer.ui.contentview;

import javax.swing.JTabbedPane;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import explorer.ui.preview.PreviewPanel;

@Component(name="Sling Explorer UI - Tabbed Pane ")
@Service(value=TabContainer.class)
public class TabContainer extends JTabbedPane {

	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;

	@Reference
	PreviewPanel panel;

	public TabContainer() {
		super(JTabbedPane.TOP);
		setFocusable(false);
	}
	
	@Activate
	private void activate(){
		addTab("Preview", null, panel, null);
		setSelectedComponent(panel);
	}

}
