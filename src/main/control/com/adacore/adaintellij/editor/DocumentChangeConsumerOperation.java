package com.adacore.adaintellij.editor;

import java.util.List;
import java.util.function.Consumer;

import com.intellij.codeInsight.folding.impl.CodeFoldingManagerImpl;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.util.ui.update.MergingUpdateQueue;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.Utils;

/**
 * Document aggregate change consumer operation.
 * Operations of this type are not tied to a particular document, they
 * register document listener in editor event multi-casters in order to
 * schedule executions on events from any document.
 * When an event occurs for a different document, the underlying
 * merging queue is flushed, immediately executing scheduled executions
 * for the previous document, before the incoming event is processed.
 */
public final class DocumentChangeConsumerOperation
	extends ConsumerOperation<DocumentEvent>
{

	/**
	 * Editor event multi-caster.
	 */
	private static final EditorEventMulticaster EVENT_MULTICASTER =
		EditorFactory.getInstance().getEventMulticaster();

	/**
	 * Document change listener for scheduling operation executions
	 * on document changes.
	 */
	private DocumentListener documentListener = new AdaDocumentListener() {

		/**
		 * @see com.adacore.adaintellij.editor.AdaDocumentListener#adaDocumentChanged(DocumentEvent)
		 */
		@Override
		public void adaDocumentChanged(@NotNull DocumentEvent event) {
			schedule(event);
		}

	};

	/**
	 * Constructs a new DocumentChangeConsumerOperation given a
	 * scheduler and a consumer.
	 * @param scheduler The scheduler responsible for the created
	 *                  operation.
	 * @param consumer The consumer to run when the created operation
	 *                 is executed.
	 */
	DocumentChangeConsumerOperation(
		@NotNull BusyEditorAwareScheduler      scheduler,
		@NotNull Consumer<List<DocumentEvent>> consumer
	) {
		super(scheduler, consumer.andThen(new Consumer<List<DocumentEvent>>() {
			@Override
			public void accept(List<DocumentEvent> documentEvents) {

				Editor editor = FileEditorManager
					.getInstance(
						scheduler.getProject()
					).getSelectedTextEditor();

				CodeFoldingManagerImpl
					.getInstance(scheduler.getProject())
					.scheduleAsyncFoldingUpdate(editor);
			}
		}));
		

		EVENT_MULTICASTER.addDocumentListener(documentListener);

	}

	/**
	 * Schedules an execution of this document event consumer operation
	 * with the given document event.
	 * This method is typically only called from the document listener
	 * owned by this class.
	 *
	 * @param event The document event to be consumed.
	 */
	@Override
	public void schedule(@NotNull DocumentEvent event) {

		// If this operation is not active, then return

		if (!isActive()) { return; }

		// If the event document is different than that of the
		// events currently scheduled to be consumed, then flush
		// the internal merging queue and clear the event list

		if (!scheduledValues.isEmpty() && !Utils.documentsRepresentSameFile(
			scheduledValues.get(0).getDocument(), event.getDocument()))
		{
			queue.flush();
			scheduledValues.clear();
		}

		// Schedule an execution with the event

		super.schedule(event);

	}

	/**
	 * @see com.adacore.adaintellij.editor.BusyEditorAwareOperation#cleanUp()
	 */
	@Override
	void cleanUp() {
		EVENT_MULTICASTER.removeDocumentListener(documentListener);
	}


	public MergingUpdateQueue getQueue()
	{
		return this.queue;
	}

}
