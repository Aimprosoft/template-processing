package com.aimprosoft.templateProcessor.utils.propertyConverter.actions.impl;

import com.aimprosoft.templateProcessor.utils.propertyConverter.actions.Action;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <h2>Implementation of {@link Action}</h2>
 */
public class DateAction implements Action {

    /* Date Format */
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Converts Alfresco {@link Date} to {@link String} using
     * pattern "dd/MM/yyyy"
     *
     * @param value value to convert
     * @return converted value
     */
    @Override
    public String execute(Serializable value) {
        return dateFormat.format((Date)value);
    }
}
