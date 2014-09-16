package flack.commands.api;

import org.apache.felix.scr.annotations.Reference;

import flack.control.DispatcherDefaultImpl;
import flack.control.api.Dispatcher;
import flack.control.api.Event;

/**
 * The Chained Command allows you to fire off one event
 * directly after you have finish processing another.
 * 
 * There is no built in mechanism to call fireNextEvent
 * 
 * The execution of this method is left up to the implementor
 * of the subclass
 * 
 * @author jason bailey
 *
 */
public abstract class ChainedCommand implements Command {
	
	@Reference
	Dispatcher dispatcher;
	
	private Event next;
	
	public ChainedCommand(Event next) {
		super();
		this.next = next;
	}


	@Override
	public abstract void process(Event event);
	
	public void fireNextEvent(){
		if (next != null){
			dispatcher.dispatchEvent(next);
		}
	}

}
