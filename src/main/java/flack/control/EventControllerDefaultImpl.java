package flack.control;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import flack.commands.api.Command;

public class EventControllerDefaultImpl implements EventListener, EventController {
	
	private Map<Class<? extends EventDefaultImpl>, Command> commands = new HashMap<Class<? extends EventDefaultImpl>, Command>();
	
	public EventControllerDefaultImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see flack.control.EventController#addCommand(java.lang.Class, flack.commands.api.Command)
	 */
	@Override
	public void addCommand(Class<? extends EventDefaultImpl>event, Command command) {
		commands.put(event, command);
		DispatcherDefaultImpl.getInstance().addController(event, this);
	}
	
	/* (non-Javadoc)
	 * @see flack.control.EventController#executeCommand(flack.control.EventDefaultImpl)
	 */
	@Override
	public void executeCommand(EventDefaultImpl event){
		Command command = commands.get(event.getClass());
		if ( command != null) {
			command.process(event);
		}
	}
	
}
