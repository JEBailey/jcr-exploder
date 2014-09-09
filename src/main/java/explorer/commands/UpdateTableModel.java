package explorer.commands;

import org.apache.sling.api.resource.Resource;

import explorer.ide.table.JcrTableModelImpl;
import flack.commands.Command;
import flack.control.Event;

public class UpdateTableModel implements Command {

	
	private JcrTableModelImpl table;
	
	public UpdateTableModel(JcrTableModelImpl table) {
		super();
		this.table = table;
	}

	@Override
	public void process(Event event) {
		table.setResource((Resource)event.getData());
		table.fireTableDataChanged();
	}

}
