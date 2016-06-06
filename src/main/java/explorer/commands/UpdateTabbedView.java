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
package explorer.commands;

import static org.osgi.service.event.EventConstants.EVENT_TOPIC;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.ui.ContentRenderer;
import explorer.ui.EventTypes;
import explorer.ui.contentview.ButtonTabComponent;
import explorer.ui.contentview.TabContainer;

@Component(name = "Sling Explorer Command - Update Editor Pane ")
@Service
@Properties(value = { @Property(name = EVENT_TOPIC, value = EventTypes.VIEW_SELECTION) })
public class UpdateTabbedView implements EventHandler {
    
	@Reference
	TabContainer editor;
	
	@Reference
	ContentRenderer contentRender;
	
    private final Logger log = LoggerFactory.getLogger(getClass());
	

	private Map<String, Object> tabs = new HashMap<String, Object>();
	
	public static final String searchFilter = "(mimeType=%s)";

	@Override
	public void handleEvent(org.osgi.service.event.Event event) {
		Resource resource = (Resource) event.getProperty("data");
		if (resource.getPath().endsWith("jcr:content")){
			resource = resource.getParent();
		}
		Object view = tabs.get(resource.getPath());
		
		if (view != null) {
			int index = editor.indexOfComponent((java.awt.Component)view);
			if (index >= 0){
				editor.setSelectedComponent((java.awt.Component) view);
				return;
			}
		} else {
			editor.setSelectedIndex(-1);
		}

		String syntax = contentRender.mimeType(resource);
		if (syntax == null || syntax.isEmpty()){
			return;
		}
		
		String filterString = String.format(searchFilter,syntax);
		
		java.awt.Component component = contentRender.getComponent(resource, filterString, syntax);
		
		editor.addTab(resource.getName(), null, component, null);
		editor.setSelectedComponent(component);
		editor.setTabComponentAt(editor.indexOfComponent(component), new ButtonTabComponent(editor));
		tabs.put(resource.getPath(),component);
	}
	






}
