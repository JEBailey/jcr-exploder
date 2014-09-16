package flack.control.api;


public interface Dispatcher {

	/**
	 * Dispatches an event.
	 */
	  boolean dispatchEvent(Event event);

	
	/**
	 * Dispatches event asynchronously into the SwingInvoker
	 * 
	 * @param event
	 */
	  void asynchEvent(Event event);

	/**
	 * Returns whether an event controller exists.
	 */
	  boolean hasController(String type);
	  
	  public void addController(Class<?> type, EventController listener);

}