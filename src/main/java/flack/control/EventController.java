package flack.control;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import flack.commands.Command;

public class EventController implements EventListener {
	
	private Map<Class<? extends Event>, Command> commands = new HashMap<Class<? extends Event>, Command>();
	
	public EventController() {
		super();
	}

	public void addCommand(Class<? extends Event>event, Command command) {
		commands.put(event, command);
		Dispatcher.getInstance().addController(event, this);
	}
	
	public void executeCommand(Event event){
		Command command = commands.get(event.getClass());
		if ( command != null) {
			command.process(event);
		}
	}
	
}
