package com.aimprosoft.templateProcessor.service;

import com.aimprosoft.templateProcessor.exceptions.FillPDFFromFormMetaDataException;
import com.aimprosoft.templateProcessor.service.pdf.form.FillPDFFormService;
import com.aimprosoft.templateProcessor.util.TemplateProcessorModel;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>Implementation of {@code NodeServicePolicies.OnUpdatePropertiesPolicy}</p>
 *
 * <p>When the properties updates the {@link TemplateProcessorModelBehaviorHandler#onUpdateProperties(NodeRef, Map, Map)}
 * via the {@link FillPDFFormService} fills values in the <span>PDF-document</span>
 * updates values in </p>
 *
 * @see NodeServicePolicies.OnUpdatePropertiesPolicy
 */
public class TemplateProcessorModelBehaviorHandler implements NodeServicePolicies.OnUpdatePropertiesPolicy {

    /* Dependencies */
    private PolicyComponent policyComponent;
    private FillPDFFormService fillPDFFormService;

    /* Behaviours */
    private Behaviour updateBehaviour;

    /* Static fields */
    private final static String UPDATE_PROPERTIES = "onUpdateProperties";

    /* Messages */
    private final static String ERROR_MESSAGE = "Unable to fill document from metadata";
    private final static String LOG_MESSAGE = "Filling PDF with messages finished OK";

    /* Logger */
    private static Logger logger = Logger.getLogger(TemplateProcessorModelBehaviorHandler.class);

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
                QName.createQName(TemplateProcessorModel.NAMESPACE_URI, TemplateProcessorModel.TYPE),
                this.updateBehaviour
        );
    }

    /**
     * Called after a node's properties have been changed.
     * After the properties has been set - the actual work is being done
     * via {@link FillPDFFormService#fillPdfFormFromMetaData(NodeRef, Map)}
     *
     * @param nodeRef reference to the updated node
     * @param oldProp the node's properties before the change
     * @param newProp the node's properties after the change
     *
     * @see FillPDFFormService
     */
    @Override
    public void onUpdateProperties(NodeRef nodeRef, Map<QName, Serializable> oldProp, Map<QName, Serializable> newProp) {
        try {
            fillPDFFormService.fillPdfFormFromMetaData(nodeRef, newProp);
        } catch (FillPDFFromFormMetaDataException e) {
            logger.error(ERROR_MESSAGE, e);
        }
        logger.debug(LOG_MESSAGE);
    }

    /* Setters */
    public void setFillPDFFormService(FillPDFFormService fillPDFFormService) {
        this.fillPDFFormService = fillPDFFormService;
    }
    public void setPolicyComponent(PolicyComponent policyComponent) {
        this.policyComponent = policyComponent;
    }
}