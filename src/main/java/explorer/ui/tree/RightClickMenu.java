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
package explorer.ui.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;

@Component(name = "Sling Explorer UI - Tree Menu")
@Service(value = RightClickMenu.class)
@SuppressWarnings("serial")
public class RightClickMenu extends JPopupMenu {

	@Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = AbstractAction.class, bind = "bindAction", unbind = "unbindAction")
	List<AbstractAction> abstractActions;

	@Activate
	public void activate() {
		for (AbstractAction action : abstractActions) {
			add(new JMenuItem(action));
		}

	}

	public void bindAction(AbstractAction filter) {
		if (abstractActions == null) {
			abstractActions = new ArrayList<AbstractAction>();
		}
		abstractActions.add(filter);
	}

	public void unbindAction(AbstractAction filter) {
		abstractActions.remove(filter);
	}
}
