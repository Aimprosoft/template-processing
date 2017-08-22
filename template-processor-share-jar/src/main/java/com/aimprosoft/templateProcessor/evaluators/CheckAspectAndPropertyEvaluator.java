package com.aimprosoft.templateProcessor.evaluators;

import org.alfresco.web.evaluator.BaseEvaluator;
import org.json.simple.JSONObject;

/**
 * Checks if the document has text and the value of the aspects property
 *
 * @see BaseEvaluator
 */
public class CheckAspectAndPropertyEvaluator extends BaseEvaluator {

    /* Aspect */
    private String aspect;
    /* Property */
    private String property;

    /**
     * <p>Checks if the document has the aspect {@link CheckAspectAndPropertyEvaluator#aspect}
     * and the value of the property {@link CheckAspectAndPropertyEvaluator#property}
     * is {@code false}</p>
     *
     * @param jsonObject object with properties
     * @return {@code true} if the aspect is present and the value of the aspects property is {@code false},
     * otherwise it returns {@code false}
     */
    @Override
    public boolean evaluate(JSONObject jsonObject) {
        return getNodeAspects(jsonObject).contains(aspect)
                && "false".equals(getProperty(jsonObject, property));
    }

    /* Setters*/
    public void setProperty(String property) {
        this.property = property;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }
}
