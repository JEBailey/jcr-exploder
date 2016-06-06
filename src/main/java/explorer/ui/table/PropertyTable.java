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
package explorer.ui.table;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

@SuppressWarnings("serial")
@Component
@Service(value = JTable.class)
public class PropertyTable extends JTable {

	@Reference
	private JcrTableModelImpl tableModel;
	
	@Activate
	private void activate(){
		setFocusable(false);
		setIntercellSpacing(new Dimension(0, 1));
		setBounds(new Rectangle(1, 1, 1, 1));
		setShowVerticalLines(false);
		setModel(tableModel);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
}
