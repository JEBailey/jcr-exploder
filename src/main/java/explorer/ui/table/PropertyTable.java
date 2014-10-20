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
