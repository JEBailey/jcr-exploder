package explorer.commands;

import org.apache.sling.api.resource.Resource;

import explorer.ide.table.JcrTableModelImpl;
import flack.commands.api.Command;
import flack.control.EventDefaultImpl;

public class UpdateTableModel implements Command {

	
	private JcrTableModelImpl table;
	
	public UpdateTableModel(JcrTableModelImpl table) {
		super();
		this.table = table;
	}

	@Override
	public void process(EventDefaultImpl event) {
		table.setResource((Resource)event.getData());
		table.fireTableDataChanged();
	}

}
