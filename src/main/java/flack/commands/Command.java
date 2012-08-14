package flack.commands;

import flack.control.Event;

public interface Command {
	
	public void process(Event event);

}
