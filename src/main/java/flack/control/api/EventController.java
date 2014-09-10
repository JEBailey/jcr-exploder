package flack.control.api;

import flack.commands.api.Command;
import flack.control.EventDefaultImpl;

public interface EventController {

	public abstract void addCommand(Class<? extends EventDefaultImpl> event, Command command);

	public abstract void executeCommand(EventDefaultImpl event);

}