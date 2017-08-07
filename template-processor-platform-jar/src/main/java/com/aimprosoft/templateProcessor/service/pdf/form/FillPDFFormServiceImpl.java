package com.aimprosoft.templateProcessor.service.pdf.form;

import com.aimprosoft.templateProcessor.exceptions.FillPDFFromFormMetaDataException;
import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link FillPDFFormService}
 *
 * @see FillPDFFormService
 */
public class FillPDFFormServiceImpl implements FillPDFFormService {

    /* Alfresco Content Service*/
    private ContentService contentService;

    /**
     * <p>{@code fillPdfFormFromMetaData()} fills values to <span>PDF-document</span>
     * with given {@code nodeRef} from Form with given {@code propertyMap}.</p>
     * <p>
     * <p> The <span>PDF-document</span> should contain input fields that are marked with
     * unique IDs. This method searches for the IDs, takes them and for each ID
     * searches for the values in the propertyMap, that presented as {@code QName, Serializable}
     * map. If {@code QName} has a local name that matches ID, the method takes its value
     * and put in the PDAcroForm field {@link org.apache.pdfbox.pdmodel.interactive.form.PDField}.</p>
     *
     * @param nodeRef     nodeRef of the given <span>PDF-document</span>
     * @param propertyMap map of the values(properties) to fill to the <span>PDF-document</span>
     * @throws IOException         thrown when problems occurred with I/O in Alfresco repository
     * @throws COSVisitorException thrown when problems occurred with {@link org.apache.pdfbox.pdmodel.PDDocument}
     * @see PDDocument
     * @see PDAcroForm
     */
    @Override
    public void fillPdfFormFromMetaData(NodeRef nodeRef, Map<QName, Serializable> propertyMap)
            throws FillPDFFromFormMetaDataException {
        try {
            ContentReader cr = contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
            ContentWriter cw = contentService.getWriter(nodeRef, ContentModel.PROP_CONTENT, true);
            PDDocument pdDocument = PDDocument.load(cr.getContentInputStream());
            PDAcroForm acroForm = pdDocument.getDocumentCatalog().getAcroForm();

        /*
            For each PDField field in PDF-document search for related QName name's local name
            and set the values into PDDocument AcroForm field if exists.
         */
            for (PDField field : (List<PDField>) acroForm.getFields()) {
                for (QName name : propertyMap.keySet()) {
                    if (name.getLocalName().equals(field.getPartialName())) {
                        field.setValue(propertyMap.get(name).toString());
                    }
                }
            }

            pdDocument.save(cw.getContentOutputStream());
        } catch (IOException | COSVisitorException e) {
            throw new FillPDFFromFormMetaDataException(e.getMessage());
        }
    }

    /* Setters */
    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }
}
