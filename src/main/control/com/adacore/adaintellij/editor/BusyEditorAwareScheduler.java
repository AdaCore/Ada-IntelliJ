package com.adacore.adaintellij.editor;

import java.util.*;
import java.util.function.Consumer;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.*;

/**
 * Project component acting as an operation scheduler that is aware of
 * the editor state.
 * The scheduler works with instances of `BusyEditorAwareOperation` which
 * represents an operation that can be executed repeatedly.
 * To improve speed and responsiveness, the scheduler continuously merges
 * incoming scheduled executions of a given operation into a single
 * execution and keeps postponing that execution until the editor is idle
 * for a configurable duration of time.
 *
 * @see com.intellij.util.ui.update.MergingUpdateQueue
 */
public final class BusyEditorAwareScheduler implements ProjectComponent {

	/**
	 * The default operation merge timeout.
	 */
	final static int DEFAULT_TIMEOUT = 800;

	/**
	 * The set of currently active operations.
	 */
	private Set<BusyEditorAwareOperation> operations = new HashSet<>();

	/**
	 * Editor event multi-caster.
	 */
	private static final EditorEventMulticaster EVENT_MULTICASTER =
		EditorFactory.getInstance().getEventMulticaster();

	/**
	 * The project to which this component belongs.
	 */
	private Project project;

	/**
	 * Constructs a new BusyEditorAwareScheduler given a project.
	 *
	 * @param project The project to attach to the constructed scheduler.
	 */
	public BusyEditorAwareScheduler(@NotNull Project project) {
		this.project = project;
	}

	/**
	 * @see com.intellij.openapi.components.NamedComponent#getComponentName()
	 */
	@NotNull
	@Override
	public String getComponentName() {
		return "com.adacore.adaintellij.editor.BusyEditorAwareScheduler";
	}

	/**
	 * @see com.intellij.openapi.components.ProjectComponent#projectOpened()
	 */
	@Override
	public void projectOpened() {

		// Register document-focus change listener

		MessageBus messageBus = project.getMessageBus();

		FileEditorManagerListener listener = new FileEditorManagerListener() {

			@Override
			public void selectionChanged(@NotNull FileEditorManagerEvent event) {
				operations.forEach(operation -> operation.queue.flush());
			}

		};

		messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, listener);

		// Register document change listener

		EVENT_MULTICASTER.addDocumentListener(new AdaDocumentListener() {

			@Override
			public void adaDocumentChanged(@NotNull DocumentEvent event) {
				operations.forEach(operation -> operation.queue.restartTimer());
			}

		});

	}

	/**
	 * Returns the BusyEditorAwareScheduler project component of the
	 * given project.
	 *
	 * @param project The project for which to get the component.
	 * @return The project component.
	 */
	@NotNull
	public static BusyEditorAwareScheduler getInstance(@NotNull Project project) {
		return project.getComponent(BusyEditorAwareScheduler.class);
	}

	/**
	 * Creates and returns a new `RunnableOperation` with the given
	 * runnable.
	 * @see com.adacore.adaintellij.editor.RunnableOperation
	 *
	 * @param runnable The runnable to run when the created operation
	 *                 is executed.
	 * @return a new `RunnableOperation` based on the given runnable.
	 */
	@Contract("_ -> new")
	@NotNull
	public RunnableOperation createRunnableOperation(@NotNull Runnable runnable) {
		return addAndReturn(new RunnableOperation(this, runnable));
	}

	/**
	 * Creates and returns a new `RunnableOperation` with the given
	 * runnable and merge timeout.
	 * @see com.adacore.adaintellij.editor.RunnableOperation
	 *
	 * @param runnable The runnable to run when the created operation
	 *                 is executed.
	 * @param timeout The merge timeout of the created operation.
	 * @return a new `RunnableOperation` based on the given runnable.
	 */
	@Contract("_, _ -> new")
	@NotNull
	public RunnableOperation createRunnableOperation(@NotNull Runnable runnable, int timeout) {
		return addAndReturn(new RunnableOperation(this, timeout, runnable));
	}

	/**
	 * Creates and returns a new `ConsumerOperation` with the given
	 * consumer.
	 * @see com.adacore.adaintellij.editor.ConsumerOperation
	 *
	 * @param consumer The consumer to run when the created operation
	 *                 is executed.
	 * @param <T> The value type of the created `ConsumerOperation`.
	 * @return a new `ConsumerOperation` based on the given consumer.
	 */
	@Contract("_ -> new")
	@NotNull
	public <T> ConsumerOperation<T> createConsumerOperation(
		@NotNull Consumer<List<T>> consumer
	) { return addAndReturn(new ConsumerOperation<>(this, consumer)); }

	/**
	 * Creates and returns a new `ConsumerOperation` with the given
	 * consumer and merge timeout.
	 * @see com.adacore.adaintellij.editor.ConsumerOperation
	 *
	 * @param consumer The consumer to run when the created operation
	 *                 is executed.
	 * @param timeout The merge timeout of the created operation.
	 * @param <T> The value type of the created `ConsumerOperation`.
	 * @return a new `ConsumerOperation` based on the given consumer.
	 */
	@Contract("_, _ -> new")
	@NotNull
	public <T> ConsumerOperation<T> createConsumerOperation(
		@NotNull Consumer<List<T>> consumer,
		         int               timeout
	) { return addAndReturn(new ConsumerOperation<>(this, timeout, consumer)); }

	/**
	 * Creates and returns a new `DocumentChangeConsumerOperation`
	 * with the given consumer.
	 * @see DocumentChangeConsumerOperation
	 *
	 * @param consumer The consumer to run when the created operation
	 *                 is executed.
	 * @return a new `DocumentChangeConsumerOperation` based on the
	 *         given consumer.
	 */
	@Contract("_ -> new")
	@NotNull
	public DocumentChangeConsumerOperation createDocumentChangeOperation(
		@NotNull Consumer<List<DocumentEvent>> consumer
	) { return addAndReturn(new DocumentChangeConsumerOperation(this, consumer)); }

	/**
	 * Removes the given operation from the set of currently active
	 * operations.
	 *
	 * @param operation The operation to remove.
	 */
	void removeOperation(
		@NotNull BusyEditorAwareOperation operation
	) { operations.remove(operation); }

	/**
	 * Returns the given operation after adding it to the set of
	 * currently active operations.
	 *
	 * @param operation The operation to add.
	 * @param <OperationType> The type of operation to add.
	 * @return The given operation.
	 */
	@Contract("_ -> param1")
	@NotNull
	private <OperationType extends BusyEditorAwareOperation>
		OperationType addAndReturn(@NotNull OperationType operation)
	{

		operations.add(operation);

		return operation;

	}

	public Project getProject()
	{
		return this.project;
	}
}
