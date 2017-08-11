package com.aimprosoft.templateProcessor.web.scripts;

import com.aimprosoft.templateProcessor.exceptions.TemplateProcessingException;
import com.aimprosoft.templateProcessor.service.pdf.form.TemplateService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.io.IOException;

/**
 * Java Web-script.
 * Executes filling values in PDF-document
 */
public class TemplateProcessorScript extends AbstractWebScript {

    /* Alfresco services*/
    private TemplateService service;
    private NodeService nodeService;

    /* Logger */
    private static Log logger = LogFactory.getLog(TemplateProcessorScript.class);

    /* Messages */
    private static final String RESPONSE_MSG = "Fill PDF-document completed.";
    private static final String ERROR_MSG = "Unable to fill values.";
    private static final String REQUEST_PARAM = "nodeRef";

    /**
     * Executes web-script
     *
     * @param req request
     * @param res response
     * @throws IOException thrown when unexpected errors occur
     */
    @Override
    public void execute(WebScriptRequest req, WebScriptResponse res) throws IOException {
        try {
            final JSONObject obj = new JSONObject();
            final NodeRef nodeRef = new NodeRef(req.getParameter(REQUEST_PARAM));
            if (!nodeService.exists(nodeRef)) {
                throw new WebScriptException("Node doesn't exist");
            }
            service.fillTemplate(nodeRef);
            obj.put("message", RESPONSE_MSG);
            res.getWriter().write(obj.toString());
        } catch (JSONException | TemplateProcessingException e) {
            logger.error(ERROR_MSG);
            throw new WebScriptException(e.getMessage(), e);
        } finally {
            logger.debug(RESPONSE_MSG);
        }
    }

    /* Setters */
    public void setService(TemplateService service) {
        this.service = service;
    }
    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }
}
