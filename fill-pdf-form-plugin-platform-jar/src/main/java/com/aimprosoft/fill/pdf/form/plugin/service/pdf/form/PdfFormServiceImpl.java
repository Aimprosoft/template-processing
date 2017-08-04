package com.aimprosoft.fill.pdf.form.plugin.service.pdf.form;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PdfFormServiceImpl implements PdfFormService {

    private ContentService contentService;

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    @Override
    public void fillPdfFormFromMetaData(NodeRef nodeRef, Map<QName, Serializable> propertyMap) throws IOException {
        ContentReader cr = contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);

        PDDocument pdDocument = PDDocument.load(cr.getContentInputStream());
        PDDocumentCatalog documentCatalog = pdDocument.getDocumentCatalog();
        PDAcroForm acroForm = documentCatalog.getAcroForm();

        for (PDField field : (List<PDField>) acroForm.getFields()) {
            for (QName n : propertyMap.keySet()) {
                if (n.getLocalName().equals(field.getPartialName())) {
                    field.setValue(propertyMap.get(n).toString());
                }
            }
        }
        pdDocument.save();

        System.out.println("ACTUAL PDF-CONVERTION");

    }
}













