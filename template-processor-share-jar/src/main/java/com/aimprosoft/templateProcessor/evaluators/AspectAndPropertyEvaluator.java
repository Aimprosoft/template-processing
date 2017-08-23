package com.aimprosoft.templateProcessor.evaluators;

import org.alfresco.web.evaluator.BaseEvaluator;
import org.json.simple.JSONObject;

import java.util.Objects;

/**
 * Checks if the document has aspect and the value of its property
 *
 * @see BaseEvaluator
 */
public class AspectAndPropertyEvaluator extends BaseEvaluator {

    /* Aspect */
    private String aspect;
    /* Property */
    private String propertyName;
    /* Required property value */
    private String propertyValue;

    /**
     * <p>Checks if the document has the aspect {@link AspectAndPropertyEvaluator#aspect}
     * and the value of the property {@link AspectAndPropertyEvaluator#propertyName}
     * is {@code false}</p>
     *
     * @param jsonObject object with properties
     * @return {@code true} if the aspect is present and the value of the aspects propertyName is {@code false},
     * otherwise it returns {@code false}
     */
    @Override
    public boolean evaluate(JSONObject jsonObject) {
        return getNodeAspects(jsonObject).contains(aspect)
                && Objects.equals(propertyValue, getProperty(jsonObject, propertyName));
    }

    /* Setters*/
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
