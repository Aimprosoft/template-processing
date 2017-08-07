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
import org.apache.pdfbox.exceptions.COSVisitorException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

public class FillPdfModelBehaviorHandler implements NodeServicePolicies.OnUpdatePropertiesPolicy {

    //Dependencies
    private PolicyComponent policyComponent;
    private PdfFormService pdfFormService;

    public void setPdfFormService(PdfFormService pdfFormService) {
        this.pdfFormService = pdfFormService;
    }

    public void setPolicyComponent(PolicyComponent policyComponent) {
        this.policyComponent = policyComponent;
    }

    // Behaviours
    private Behaviour onUpdateProperties;

    public void init(){
        this.onUpdateProperties = new JavaBehaviour(this,"onUpdateProperties",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);

        this.policyComponent.bindClassBehaviour(
                QName.createQName(NamespaceService.ALFRESCO_URI, "onUpdateProperties"),
                QName.createQName(FillPdfModel.NAMESPACE_URI, FillPdfModel.TYPE),
                this.onUpdateProperties
        );

    }

    @Override
    public void onUpdateProperties(NodeRef nodeRef, Map<QName, Serializable> oldProp, Map<QName, Serializable> newProp) {
        try {
            pdfFormService.fillPdfFormFromMetaData(nodeRef, newProp);
        } catch (IOException | COSVisitorException e) {
            e.printStackTrace();
        }
        System.out.println("UPDATING PDF FINISHED");
    }
}











