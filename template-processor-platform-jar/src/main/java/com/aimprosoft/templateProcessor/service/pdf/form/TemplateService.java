package com.aimprosoft.templateProcessor.service.pdf.form;

import com.aimprosoft.templateProcessor.exceptions.TemplateProcessingException;
import org.alfresco.service.cmr.repository.NodeRef;

/**
 * <h2>Service that fills <span>PDF-document</span> with values.</h2>
 * <p>
 * <p>The PDF should have input fields with unique IDs,
 * that this service uses to get values from Alfresco properties.</p>
 * <p>
 * <p>The only method {@code fillTemplate()} does all job for
 * filling data.</p>
 */
public interface TemplateService {

    /**
     * {@code fillTemplate()} fills values to <span>PDF-document</span>s
     * fields from the documents properties.
     *
     * @param nodeRef nodeRef of the given <span>PDF-document</span>
     * @throws TemplateProcessingException thrown when values don't match requirements
     */
    void fillTemplate(String nodeRef) throws TemplateProcessingException;

}
