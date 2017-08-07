package com.aimprosoft.templateProcessor.service.pdf.form;

import com.aimprosoft.templateProcessor.exceptions.FillPDFFromFormMetaDataException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.pdfbox.exceptions.COSVisitorException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * <h2>Service that fills <span>PDF-document</span> with values.</h2>
 * <p>
 * <p>The PDF should have input fields with unique IDs,
 * that this service uses to get values from Alfresco properties.</p>
 * <p>
 * <p>The only method {@code fillPdfFormFromMetaData()} does all job for
 * filling data.</p>
 */
public interface FillPDFFormService {

    /**
     * {@code fillPdfFormFromMetaData()} fills values to <span>PDF-document</span>'s
     * fields from the form with {@code propertyMap}'s values.
     *
     * @param nodeRef     nodeRef of the given <span>PDF-document</span>
     * @param propertyMap map of the values(properties) to fill to the <span>PDF-document</span>
     * @throws IOException         thrown when problems occurred with I/O in Alfresco repository
     * @throws COSVisitorException thrown when problems occurred with {@link org.apache.pdfbox.pdmodel.PDDocument}
     */
    void fillPdfFormFromMetaData(NodeRef nodeRef, Map<QName, Serializable> propertyMap) throws FillPDFFromFormMetaDataException;

}
