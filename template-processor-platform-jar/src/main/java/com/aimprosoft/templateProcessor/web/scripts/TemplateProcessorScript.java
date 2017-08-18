package com.aimprosoft.templateProcessor.web.scripts;

import com.aimprosoft.templateProcessor.exceptions.TemplateProcessingException;
import com.aimprosoft.templateProcessor.service.pdf.form.TemplateService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Java Web-script.
 * Executes filling values in PDF-document
 */
public class TemplateProcessorScript extends DeclarativeWebScript {

    /* Alfresco services*/
    private TemplateService service;
    private NodeService nodeService;

    /* Logger */
    private static Log logger = LogFactory.getLog(TemplateProcessorScript.class);

    /* Messages */
    private static final String RESPONSE_MSG = "Fill PDF-document completed.";
    private static final String ERROR_MSG = "Error converting values into PDF-document";
    private static final String REQUEST_PARAM = "nodeRef";

    /**
     * Executes Web-script
     *
     * @param req request
     * @param status status
     * @param cache cache
     * @return {@link Map model}
     */
    public Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
        Map<String, Object> model = new HashMap<>();
        try {
            NodeRef nodeRef = new NodeRef(req.getParameter(REQUEST_PARAM));
            service.fillTemplate(nodeRef);
            model.put("message", RESPONSE_MSG);
        } catch (TemplateProcessingException e) {
            logger.error(ERROR_MSG, e);
            throw new WebScriptException(ERROR_MSG, e);
        }
        return model;
    }

    /* Setters */
    public void setService(TemplateService service) {
        this.service = service;
    }
    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }
}
