package com.aimprosoft.templateProcessor.utils.propertyConverter.actions;

import java.io.Serializable;

/**
 * <p>Presents a common interface for the actions provided by
 * helper class {@link com.aimprosoft.templateProcessor.utils.propertyConverter.Converter}</p>
 */
public interface Action {

    /**
     * Converts a {@link Serializable value} to its {@link String} presentation
     *
     * @param value value to convert
     * @return converted value
     */
    String execute(Serializable value);
}
