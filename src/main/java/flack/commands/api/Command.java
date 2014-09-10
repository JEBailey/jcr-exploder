package flack.commands.api;

import flack.control.EventDefaultImpl;

public interface Command {
	
	public void process(EventDefaultImpl event);

}
