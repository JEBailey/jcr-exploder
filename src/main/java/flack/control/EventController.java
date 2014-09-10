package flack.control;

import flack.commands.api.Command;

public interface EventController {

	public abstract void addCommand(Class<? extends EventDefaultImpl> event, Command command);

	public abstract void executeCommand(EventDefaultImpl event);

}