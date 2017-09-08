package com.aimprosoft.templateProcessor.utils.propertyConverter.actions.impl;

import com.aimprosoft.templateProcessor.utils.propertyConverter.actions.Action;

import java.io.Serializable;

/**
 * <h2>Implementation of {@link Action}</h2>
 */
public class BooleanAction implements Action {

    /** Value of {@code true} for PDF-document */
    private final static String trueValue = "Yes";
    /** Value of {@code false} for PDF-document */
    private final static String falseValue = "Off";

    /**
     * Converts Alfresco {@link Boolean} to {@link String} according to
     * its PDF required value
     *
     * @param value value to convert
     * @return converted value
     */
    @Override
    public String execute(Serializable value) {
        return (Boolean)value ? trueValue : falseValue;
    }
}
