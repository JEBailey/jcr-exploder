package flack.commands.api;

import flack.control.api.Event;

public interface Command {
	
	public void process(Event event);

}
