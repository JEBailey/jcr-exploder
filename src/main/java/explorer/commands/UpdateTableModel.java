package explorer.commands;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;

import explorer.ide.table.JcrTableModelImpl;
import flack.commands.api.Command;
import flack.control.EventDefaultImpl;

@Component(name="Sling Explorer Command - Update Table",description="Updates Property Table")
@Service
@Property(name="type", value="updateTable")
public class UpdateTableModel implements Command {

	@Reference
	private JcrTableModelImpl table;

	@Override
	public void process(EventDefaultImpl event) {
		table.setResource((Resource)event.getData());
		table.fireTableDataChanged();
	}

}
