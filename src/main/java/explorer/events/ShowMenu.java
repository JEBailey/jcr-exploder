package explorer.events;

import java.awt.Component;
import java.awt.Point;

import flack.control.EventDefaultImpl;

public class ShowMenu extends EventDefaultImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 888482320347216580L;

	public ShowMenu(Component source, Point data) {
		super(source, data);
	}

}
