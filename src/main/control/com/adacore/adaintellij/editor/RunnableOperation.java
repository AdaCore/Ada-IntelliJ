package com.adacore.adaintellij.editor;

import com.intellij.util.ui.update.Update;
import org.jetbrains.annotations.NotNull;

/**
 * Simple runnable busy-editor-aware operation.
 * @see com.adacore.adaintellij.editor.BusyEditorAwareOperation
 */
public class RunnableOperation extends BusyEditorAwareOperation {
	
	/**
	 * The underlying runnable that is run when this operation
	 * is executed.
	 */
	Runnable runnable;
	
	/**
	 * Constructs a new RunnableOperation given a scheduler and
	 * a runnable.
	 *
	 * @param scheduler The scheduler responsible for the created
	 *                  operation.
	 * @param runnable The runnable to run when the created
	 *                 operation is executed.
	 */
	RunnableOperation(
		@NotNull BusyEditorAwareScheduler scheduler,
		@NotNull Runnable                 runnable
	) {
		super(scheduler);
		this.runnable = runnable;
	}
	
	/**
	 * Constructs a new RunnableOperation given a scheduler, a
	 * merge timeout and a runnable.
	 * @param scheduler The scheduler responsible for the created
	 *                  operation.
	 * @param timeout The merge timeout of the created operation.
	 * @param runnable The runnable to run when the created
	 *                 operation is executed.
	 */
	RunnableOperation(
		@NotNull BusyEditorAwareScheduler scheduler,
		         int                      timeout,
		@NotNull Runnable                 runnable
	) {
		super(scheduler, timeout);
		this.runnable = runnable;
	}
	
	/**
	 * Schedules an execution of this runnable operation.
	 */
	public void schedule() {
		
		// If this operation is not active, then return
		
		if (!isActive()) { return; }
		
		// Schedule an execution in the internal queue
		
		queue.queue(Update.create(this, runnable));
		
	}
	
}
