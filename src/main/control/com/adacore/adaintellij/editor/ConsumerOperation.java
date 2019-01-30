package com.adacore.adaintellij.editor;

import java.util.*;
import java.util.function.Consumer;

import com.intellij.util.ui.update.Update;
import org.jetbrains.annotations.NotNull;

/**
 * Consumer busy-editor-aware operation.
 * Merging executions of such operations will result in execution
 * values being aggregated and fed together to the consumer once
 * the underlying queue times out and the operation executes.
 * @see com.adacore.adaintellij.editor.BusyEditorAwareOperation
 *
 * For an example use case of a consumer operation:
 * @see DocumentChangeConsumerOperation
 */
public class ConsumerOperation<T> extends BusyEditorAwareOperation {

	/**
	 * The underlying consumer that is run when this operation
	 * is executed.
	 */
	Consumer<List<T>> consumer;

	/**
	 * The list of scheduled execution values to be consumed.
	 */
	List<T> scheduledValues = new ArrayList<>();

	/**
	 * Constructs a new ConsumerOperation given a scheduler and
	 * a consumer.
	 *
	 * @param scheduler The scheduler responsible for the created
	 *                  operation.
	 * @param consumer The consumer to run when the created
	 *                 operation is executed.
	 */
	ConsumerOperation(
		@NotNull BusyEditorAwareScheduler scheduler,
		@NotNull Consumer<List<T>>        consumer
	) {
		super(scheduler);
		this.consumer = consumer;
	}

	/**
	 * Constructs a new ConsumerOperation given a scheduler, a
	 * merge timeout and a consumer.
	 *
	 * @param scheduler The scheduler responsible for the created
	 *                  operation.
	 * @param timeout The merge timeout of the created operation.
	 * @param consumer The consumer to run when the created
	 *                 operation is executed.
	 */
	ConsumerOperation(
		@NotNull BusyEditorAwareScheduler scheduler,
		         int                      timeout,
		@NotNull Consumer<List<T>>        consumer
	) {
		super(scheduler, timeout);
		this.consumer = consumer;
	}

	/**
	 * Schedules an execution of this consumer operation, with
	 * the given value.
	 *
	 * @param value The value to be consumed.
	 */
	public void schedule(@NotNull T value) {

		// If this operation is not active, then return

		if (!isActive()) { return; }

		boolean empty = scheduledValues.isEmpty();

		// Add the value to the list of values to be consumed

		scheduledValues.add(value);

		// If the list was empty, then schedule an execution
		// in the internal queue

		if (empty) {

			queue.queue(Update.create(this, () -> {

				consumer.accept(scheduledValues);

				scheduledValues.clear();

			}));

		}


	}

}
