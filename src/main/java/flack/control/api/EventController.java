package flack.control.api;

import flack.commands.api.Command;

public interface EventController {

	public void addCommand(Class<? extends Event> event, Command command);

	public void executeCommand(Event event);

}