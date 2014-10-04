package explorer.commands;

import static org.osgi.service.event.EventConstants.EVENT_TOPIC;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.commons.mime.MimeTypeService;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.EventHandler;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.core.api.MimeProvider;
import explorer.ui.EventTypes;
import explorer.ui.TabEditor;
import explorer.ui.tabbedView.ButtonTabComponent;

@Component(name = "Sling Explorer Command - Update Editor Pane ", description = "Updates the Editor Pane with the correct view")
@Service
@Properties(value = { @Property(name = EVENT_TOPIC, value = EventTypes.NEW_SELECTION) })
public class UpdateEditorPane implements EventHandler {

	/** default log */
    private final Logger log = LoggerFactory.getLogger(getClass());
    
	@Reference
	TabEditor editor;

	@Reference
	MimeTypeService mimes;
	

	private Map<String, Object> tabs = new HashMap<String, Object>();

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

		String syntax = mimeType(resource);
		if (syntax == null || syntax.isEmpty()){
			return;
		}
		MimeProvider provider = getMimeProvider(syntax);
		if (provider == null){
			log.error("no syntax for {}",syntax);
			return;
		}
		java.awt.Component component = provider.createComponent(resource, syntax);
		editor.addTab(resource.getName(), null, component, null);
		editor.setSelectedComponent(component);
		editor.setTabComponentAt(editor.indexOfComponent(component), new ButtonTabComponent(editor));
		tabs.put(resource.getPath(),component);
	}

	private String mimeType(Resource resource) {
		ResourceMetadata metaData = resource.getResourceMetadata();
		String prop = metaData.getContentType();
		if (prop == null) {
			prop = mimes.getMimeType(resource.getName());
		}
		if (prop == null) {
			prop = "";
		}
		prop = prop.replace("x-", "").replace("-source", "");
		return prop.replace("application/", "text/");
	}

	public MimeProvider getMimeProvider(String syntax){
		String filterString = String.format("(mimeType=%s)",syntax);
		try {
			Filter filter =  FrameworkUtil.createFilter(filterString);
			for (ServiceReference sr: tracker.getServiceReferences()){
				if (filter.match(sr)){
					return (MimeProvider)tracker.getService(sr);
				}
			}
		} catch (InvalidSyntaxException e) {
			log.warn(e.getMessage());
		}
		return null;
	}
	
	@Activate
	public void activate(ComponentContext context) throws InvalidSyntaxException{
		tracker = new ServiceTracker(context.getBundleContext(), MimeProvider.class.getName(),null);
		tracker.open();
	}
	
	@Deactivate
	public void deactivate(){
		tracker.close();
	}
	
	ServiceTracker tracker;

}
