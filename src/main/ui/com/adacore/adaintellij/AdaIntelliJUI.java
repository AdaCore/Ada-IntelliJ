package com.adacore.adaintellij;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Abstract Ada-IntelliJ UI view.
 * Any instance of this class is a reusable view, i.e. it can be embedded in
 * other Ada-IntelliJ UI views to create complex UI hierarchies. An embedded
 * view holds a reference to its parent UI in the `parentUI` attribute, and
 * therefore its constructor must call this abstract class' constructor with
 * a non-null UI representing its parent UI.
 */
public abstract class AdaIntelliJUI {
	
	/**
	 * The UI in which this view is embedded.
	 * Its value is null if this view is not embedded in another UI.
	 */
	@Nullable
	private AdaIntelliJUI parentUI;
	
	/**
	 * Constructs a new AdaIntelliJUI that is either a standalone UI
	 * if the given parent UI is null, or embedded in it otherwise.
	 *
	 * @param parentUI The parent UI in which to embed the constructed
	 *                 UI view, or null.
	 */
	public AdaIntelliJUI(@Nullable AdaIntelliJUI parentUI) {
		this.parentUI = parentUI;
	}
	
	/**
	 * Returns the root component of this UI view.
	 *
	 * @return The root UI component.
	 */
	@NotNull
	public abstract JComponent getUIRoot();
	
	/**
	 * Updates this UI view.
	 */
	protected void updateUI() {
		
		if (parentUI != null) {
			parentUI.updateUI();
		} else {
			getUIRoot().updateUI();
		}
		
	}

}
