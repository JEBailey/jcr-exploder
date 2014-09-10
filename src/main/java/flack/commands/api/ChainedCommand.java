package flack.commands.api;

import flack.control.DispatcherDefaultImpl;
import flack.control.EventDefaultImpl;

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
	
	private EventDefaultImpl next;
	
	public ChainedCommand(EventDefaultImpl next) {
		super();
		this.next = next;
	}


	@Override
	public abstract void process(EventDefaultImpl event);
	
	public void fireNextEvent(){
		if (next != null){
			DispatcherDefaultImpl.getInstance().dispatchEvent(next);
		}
	}

}
