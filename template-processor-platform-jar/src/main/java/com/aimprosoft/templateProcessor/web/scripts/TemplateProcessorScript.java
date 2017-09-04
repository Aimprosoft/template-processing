package com.aimprosoft.templateProcessor.web.scripts;

import com.aimprosoft.templateProcessor.exceptions.TemplateProcessingException;
import com.aimprosoft.templateProcessor.service.pdf.form.TemplateService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Java Web-script.
 * Executes filling values in PDF-document
 */
public class TemplateProcessorScript extends DeclarativeWebScript {

    /* Alfresco services*/
    private TemplateService service;

    /* Messages */
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
        } catch (TemplateProcessingException e) {
            status.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.put("errorMessage", e.getMessage());
        }
        return model;
    }

    /* Setters */
    public void setService(TemplateService service) {
        this.service = service;
    }
}
