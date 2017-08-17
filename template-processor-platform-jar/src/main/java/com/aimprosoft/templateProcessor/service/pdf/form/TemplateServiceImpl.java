package com.aimprosoft.templateProcessor.service.pdf.form;

import com.aimprosoft.templateProcessor.exceptions.TemplateProcessingException;
import com.aimprosoft.templateProcessor.models.TemplateProcessorModel;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.cmr.repository.*;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link TemplateService}
 */
public class TemplateServiceImpl implements TemplateService {

    /* Logger*/
    private static Log logger = LogFactory.getLog(TemplateServiceImpl.class);

    /* Alfresco Services */
    private ContentService contentService;
    private NodeService nodeService;
    private NamespaceService namespaceService;

    /**
     * <p>Fills values into the <span>PDF-document</span> using {@link Map propertyMap}</p>
     * <p>The <span>PDF-document</span> should contain input fields that are marked with
     * unique IDs. The method searches for the IDs, takes them and for each ID
     * searches for the values in the propertyMap of ({@link QName}, {@link Serializable})</p>
     * <p>If {@link QName#getPrefixString()} matches {@link PDField#getValue()}, the method takes its value
     * and puts it in the {@link PDAcroForm} field {@link PDField}.</p>
     *
     * @param nodeRef     nodeRef of the document
     * @param propertyMap properties of the document
     * @throws TemplateProcessingException thrown when serious injures occur
     */
    private void fillPdfFormFromMetaData(NodeRef nodeRef, Map<QName, Serializable> propertyMap)
            throws TemplateProcessingException {
        try {
            ContentReader cr = contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
            ContentWriter cw = contentService.getWriter(nodeRef, ContentModel.PROP_CONTENT, true);
            PDDocument pdDocument = PDDocument.load(cr.getContentInputStream());

            @SuppressWarnings("unchecked")
            List<PDField> fields = pdDocument.getDocumentCatalog().getAcroForm().getFields();
            /*
                For each PDField field in PDF-document search for related QName name's "prefixed name"
                and set the values into PDDocument AcroForm field if exists.
            */
            for (PDField field : fields) {
                String fieldName = field.getPartialName();
                String value = (String) propertyMap.get(createQName(fieldName));
                if (value != null) {
                    field.setValue(value);
                    logger.debug("Field from PDF-document was filled in: " + fieldName);
                } else {
                    logger.debug("Field from PDF-document not found in meta-data properties: " + fieldName);
                }
            }
            pdDocument.save(cw.getContentOutputStream());
        } catch (IOException | COSVisitorException e) {
            throw new TemplateProcessingException("Error converting values into PDF-document", e);
        }
    }

    /**
     * Creates {@link QName qName} from <span>PDF-document</span>'s input field's ID
     * or logs error if it's not possible to create {@code QName}
     *
     * @param name prefixed {@link QName#getPrefixString()} name
     * @return {@code QName} if it is possible to create one or
     * {@code null} if it's not
     */
    private QName createQName(String name) {
        QName qName = null;
        if (name != null && !name.equals("")) {
            qName = QName.createQName(name, namespaceService);
        }
        return qName;
    }

    /**
     * <p>Fills values to <span>PDF-document</span> with given {@code nodeRef}
     * from the properties of the document</p>
     * <p>The {@link NodeRef nodeRef} should have mime-type {@link MimetypeMap#MIMETYPE_PDF},
     * otherwise the {@link TemplateProcessingException} will be thrown.</p>
     *
     * @param nodeRef nodeRef of the given <span>PDF-document</span>
     * @throws TemplateProcessingException thrown when errors occur
     * @see PDDocument
     * @see PDAcroForm
     */
    @Override
    public void fillTemplate(NodeRef nodeRef) throws TemplateProcessingException {
        if (contentService.getReader(nodeRef, ContentModel.TYPE_CONTENT)
                .getMimetype().equals(MimetypeMap.MIMETYPE_PDF)) {
            fillPdfFormFromMetaData(nodeRef, nodeService.getProperties(nodeRef));

            /* Mark property when PDF-document is filled in with values*/
            nodeService.setProperty(nodeRef, TemplateProcessorModel.ASPECT_PROPERTY, true);
        } else {
            throw new TemplateProcessingException("Content type should be '" + MimetypeMap.MIMETYPE_PDF + "'.");
        }
    }

    /* Setters */
    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setNamespaceService(NamespaceService namespaceService) {
        this.namespaceService = namespaceService;
    }
}
