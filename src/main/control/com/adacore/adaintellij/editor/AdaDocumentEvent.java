package com.adacore.adaintellij.editor;

import com.intellij.openapi.editor.event.DocumentEvent;

public class AdaDocumentEvent {

    private DocumentEvent   documentEvent;
    private String          previousContent;

    public AdaDocumentEvent(DocumentEvent doc, String previousContent)
    {
        this.documentEvent = doc;
        this.previousContent = previousContent;
    }

    public DocumentEvent getDocumentEvent() {
        return documentEvent;
    }

    public String getPreviousContent() {
        return previousContent;
    }
}
