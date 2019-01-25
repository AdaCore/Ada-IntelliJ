package com.adacore.adaintellij.editor;

import com.intellij.openapi.util.Disposer;
import com.intellij.util.ui.update.MergingUpdateQueue;
import org.jetbrains.annotations.*;

/**
 * Generic operation that can be scheduled to execute repeatedly.
 * @see com.adacore.adaintellij.editor.BusyEditorAwareScheduler
 *
 * Different implementations have different scheduling mechanisms,
 * which is why this class does not provide a scheduling interface
 * and instead leaves it to the implementations to do so.
 */
abstract class BusyEditorAwareOperation {
	
	/**
	 * Internal merging queue holding scheduled executions.
	 */
	MergingUpdateQueue queue;
	
	/**
	 * The scheduler responsible for this operation.
	 */
	private BusyEditorAwareScheduler scheduler;
	
	/**
	 * Whether or not this operation is active.
	 */
	private boolean active = true;
	
	/**
	 * Constructs a new BusyEditorAwareOperation given a scheduler.
	 *
	 * @param scheduler The scheduler responsible for the created
	 *                  operation.
	 */
	BusyEditorAwareOperation(@NotNull BusyEditorAwareScheduler scheduler) {
		this(scheduler, BusyEditorAwareScheduler.DEFAULT_TIMEOUT);
	}
	
	/**
	 * Constructs a new BusyEditorAwareOperation given a scheduler
	 * and a merge timeout.
	 *
	 * @param scheduler The scheduler responsible for the created
	 *                  operation.
	 * @param timeout The merge timeout of the created operation.
	 */
	BusyEditorAwareOperation(@NotNull BusyEditorAwareScheduler scheduler, int timeout) {
		this.scheduler = scheduler;
		this.queue     = new MergingUpdateQueue(
			"BusyEditorAwareOperation@" + hashCode(), timeout, true, null);
	}
	
	/**
	 * Stops this operation from executing in the future.
	 */
	public final void stop() {
		
		// If this operation has already been stopped,
		// then return immediately
		
		if (!active) { return; }
		
		// Deactivate this operation
		
		active = false;
		
		// Call the extensible clean-up method
		
		cleanUp();
		
		// Perform global clean-up
		
		queue.deactivate();
		Disposer.dispose(queue);
		
		// Remove this operation from the scheduler
		
		scheduler.removeOperation(this);
		
	}
	
	/**
	 * Returns whether or not this operation is active.
	 *
	 * @return Whether or not this operation is active.
	 */
	@Contract(pure = true)
	public final boolean isActive() { return active; }
	
	/**
	 * Performs additional clean-up.
	 * This method is meant to be overridden by concrete operation
	 * implementations that require custom additional clean-up.
	 * The default implementation performs no clean-up.
	 */
	void cleanUp() {}
	
}
