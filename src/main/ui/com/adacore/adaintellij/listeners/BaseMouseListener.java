package com.adacore.adaintellij.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Mouse listener that simply ignores all mouse events.
 * Allows to easily implement mouse listeners that only handle specific
 * events without having to include empty overriding methods for the
 * remaining events.
 */
public class BaseMouseListener implements MouseListener {
	
	/**
	 * Called when the mouse is clicked.
	 *
	 * @param mouseEvent The mouse event.
	 */
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {}
	
	/**
	 * Called when the mouse is pressed.
	 *
	 * @param mouseEvent The mouse event.
	 */
	@Override
	public void mousePressed(MouseEvent mouseEvent) {}
	
	/**
	 * Called when the mouse is released.
	 *
	 * @param mouseEvent The mouse event.
	 */
	@Override
	public void mouseReleased(MouseEvent mouseEvent) {}
	
	/**
	 * Called when the mouse is entered.
	 *
	 * @param mouseEvent The mouse event.
	 */
	@Override
	public void mouseEntered(MouseEvent mouseEvent) {}
	
	/**
	 * Called when the mouse is exited.
	 *
	 * @param mouseEvent The mouse event.
	 */
	@Override
	public void mouseExited(MouseEvent mouseEvent) {}
	
}
