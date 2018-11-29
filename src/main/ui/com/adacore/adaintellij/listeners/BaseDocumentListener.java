package com.adacore.adaintellij.listeners;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Document listener that simply ignores all document events.
 * Allows to easily implement document listeners that only handle specific
 * events without having to include empty overriding methods for the
 * remaining events.
 */
public class BaseDocumentListener implements DocumentListener {
	
	/**
	 * Called when text is inserted.
	 *
	 * @param documentEvent The document event.
	 */
	@Override
	public void insertUpdate(DocumentEvent documentEvent) {}
	
	/**
	 * Called when text is removed.
	 *
	 * @param documentEvent The document event.
	 */
	@Override
	public void removeUpdate(DocumentEvent documentEvent) {}
	
	/**
	 * Called when text is replaced.
	 *
	 * @param documentEvent The document event.
	 */
	@Override
	public void changedUpdate(DocumentEvent documentEvent) {}
	
}
