package com.aimprosoft.templateProcessor.service.pdf.form;

import com.aimprosoft.templateProcessor.exceptions.TemplateProcessingException;
import com.aimprosoft.templateProcessor.models.TemplateProcessorModel;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.cmr.repository.*;
import org.alfresco.service.namespace.NamespaceException;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang3.StringUtils;
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
     * <p>Fills values to <span>PDF-document</span> with given {@code nodeRef}
     * from the properties of the document</p>
     * <p>Creates the {@link NodeRef node} and checks if it exists. If {@code String nodeRef} is
     * not valid or {@link NodeRef node} doesn't exist it throws
     * a {@link TemplateProcessingException exception}</p>
     * <p>If {@link NodeRef node} is valid - it goes to filling values in template.</p>
     * <p>The {@link NodeRef nodeRef} should have mime-type {@link MimetypeMap#MIMETYPE_PDF},
     * otherwise the {@link TemplateProcessingException} will be thrown.</p>
     *
     * @param nodeRef nodeRef of the given <span>PDF-document</span>
     * @throws TemplateProcessingException thrown when errors occur
     */
    @Override
    public void fillTemplate(NodeRef nodeRef) throws TemplateProcessingException {
        if (!nodeService.exists(nodeRef)) {
            throw new TemplateProcessingException("Node '" + nodeRef.toString() + "' doesn't exist.");
        }
        if(!nodeService.hasAspect(nodeRef, TemplateProcessorModel.ASPECT_TEMPLATE)){
            throw new TemplateProcessingException("The document doesn't have required aspect 'aim:template'.");
        }
        String mimeType = contentService.getReader(nodeRef, ContentModel.TYPE_CONTENT).getMimetype();
        String requiredMimeType = MimetypeMap.MIMETYPE_PDF;
        if (mimeType.equals(requiredMimeType)) {
            fillTemplate(nodeRef, nodeService.getProperties(nodeRef));

            /* Mark property when PDF-document is filled in with values */
            nodeService.setProperty(nodeRef, TemplateProcessorModel.PROP_PROCESSED, true);
        } else {
            String msg = "Content type should be '" + requiredMimeType + "'. " +
                    "Content type '" + mimeType + "' is not supported.";
            throw new TemplateProcessingException(msg);
        }
    }

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
     * @see PDDocument
     * @see PDAcroForm
     */
    private void fillTemplate(NodeRef nodeRef, Map<QName, Serializable> propertyMap)
            throws TemplateProcessingException {
        try {
            ContentReader cr = contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
            PDDocument pdDocument = PDDocument.load(cr.getContentInputStream());
            PDAcroForm acroForm = pdDocument.getDocumentCatalog().getAcroForm();

            if (acroForm != null) {
                @SuppressWarnings("unchecked")
                List<PDField> fields = acroForm.getFields();
                /*
                   For each PDField field in the template searches for QName's "prefixed name"
                    and set the values into PDDocument's AcroForm field if exists.
                */
                for (PDField field : fields) {
                    String fieldName = field.getPartialName();
                    String value = (String) propertyMap.get(createQName(fieldName));
                    if (value != null) {
                        field.setValue(value);
                        logger.debug("Field from template was filled in: " + fieldName);
                    } else {
                        logger.debug("Field from template wasn't found in meta-data properties: " + fieldName);
                    }
                }

                /* Save template */
                ContentWriter cw = contentService.getWriter(nodeRef, ContentModel.PROP_CONTENT, true);
                pdDocument.save(cw.getContentOutputStream());
            } else {
                logger.debug("Template doesn't have AcroForm.");
            }
        } catch (IOException | COSVisitorException e) {
            throw new TemplateProcessingException("Error occurred while filling values in the template.", e);
        }
    }

    /**
     * Creates {@link QName qName} from <span>PDF-document</span>'s input field ID
     * or logs error if it's not possible to create {@code QName}.
     *
     * @param name prefixed {@link QName#getPrefixString()} name
     * @return {@code QName} if it is possible to create one
     * or {@code null} if it's not.
     */
    private QName createQName(String name) {
        QName qName = null;
        if (StringUtils.isNotBlank(name)) {
            try {
                qName = QName.createQName(name, namespaceService);
            } catch (NamespaceException e) {
                logger.debug(name + " QName is not resolved.", e);
            }
        }
        return qName;
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
