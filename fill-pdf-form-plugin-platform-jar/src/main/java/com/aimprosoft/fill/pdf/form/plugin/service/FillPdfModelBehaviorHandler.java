package com.aimprosoft.fill.pdf.form.plugin.service;

import com.aimprosoft.fill.pdf.form.plugin.service.pdf.form.PdfFormService;
import com.aimprosoft.fill.pdf.form.plugin.util.FillPdfModel;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>Implementation of {@code NodeServicePolicies.OnUpdatePropertiesPolicy}</p>
 *
 * <p>When the properties updates the {@link FillPdfModelBehaviorHandler#onUpdateProperties(NodeRef, Map, Map)}
 * via the {@link PdfFormService} fills values in the <span>PDF-document</span>
 * updates values in </p>
 *
 * @see NodeServicePolicies.OnUpdatePropertiesPolicy
 */
public class FillPdfModelBehaviorHandler implements NodeServicePolicies.OnUpdatePropertiesPolicy {

    /* Dependencies */
    private PolicyComponent policyComponent;
    private PdfFormService pdfFormService;

    /* Behaviours */
    private Behaviour updateBehaviour;

    /* Static fields */
    private final static String UPDATE_PROPERTIES = "updateBehaviour";

    /* Messages */
    private final static String ERROR_MESSAGE = "Filling PDF with fields failed, more info: ";
    private final static String LOG_MESSAGE = "Filling PDF with messages finished OK";

    /* Logger */
    private static Logger logger = Logger.getLogger(FillPdfModelBehaviorHandler.class);

    /**
     * Creates {@link Behaviour this.updateBehavviour}
     * and binds {@link PolicyComponent this.policyComponent}
     * to the behaviour
     */
    public void init() {
        this.updateBehaviour = new JavaBehaviour(
                this,
                UPDATE_PROPERTIES,
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT
        );

        this.policyComponent.bindClassBehaviour(
                QName.createQName(NamespaceService.ALFRESCO_URI, UPDATE_PROPERTIES),
                QName.createQName(FillPdfModel.NAMESPACE_URI, FillPdfModel.TYPE),
                this.updateBehaviour
        );

    }

    /**
     * Called after a node's properties have been changed.
     * After the properties has been set - the actual work is being done
     * via {@link PdfFormService#fillPdfFormFromMetaData(NodeRef, Map)}
     *
     * @param nodeRef reference to the updated node
     * @param oldProp the node's properties before the change
     * @param newProp the node's properties after the change
     *
     * @see PdfFormService
     */
    @Override
    public void onUpdateProperties(NodeRef nodeRef, Map<QName, Serializable> oldProp, Map<QName, Serializable> newProp) {
        try {
            pdfFormService.fillPdfFormFromMetaData(nodeRef, newProp);
        } catch (IOException | COSVisitorException e) {
            logger.error(ERROR_MESSAGE + e.getMessage());
        }
        logger.log(Level.INFO, LOG_MESSAGE);
    }

    /* Setters */
    public void setPdfFormService(PdfFormService pdfFormService) {
        this.pdfFormService = pdfFormService;
    }

    public void setPolicyComponent(PolicyComponent policyComponent) {
        this.policyComponent = policyComponent;
    }

    /* !Setters */
}











