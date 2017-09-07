package com.aimprosoft.templateProcessor.utils.propertyConverter;

import com.aimprosoft.templateProcessor.utils.propertyConverter.actions.Action;
import com.aimprosoft.templateProcessor.utils.propertyConverter.actions.impl.BooleanAction;
import com.aimprosoft.templateProcessor.utils.propertyConverter.actions.impl.DateAction;
import com.aimprosoft.templateProcessor.utils.propertyConverter.actions.impl.DateTimeAction;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.namespace.QName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <h2>Helper class</h2>
 * <p>Converts a value according to its Alfresco {@link DataTypeDefinition data type}.</p>
 * <p>Implements the Strategy Pattern</p>
 *
 * @see DataTypeDefinition
 */
public class Converter {

    /* Container for available Actions */
    private Map<String, Action> actions;

    /**
     * Constructor. Creates a {@link #actions} actions of {@link QName qName}, {@link Action action}
     */
    public Converter() {
        actions = new HashMap<>();

        actions.put(DataTypeDefinition.DATE.getLocalName(), new DateAction());
        actions.put(DataTypeDefinition.DATETIME.getLocalName(), new DateTimeAction());
        actions.put(DataTypeDefinition.BOOLEAN.getLocalName(), new BooleanAction());
    }

    /**
     * Converts a value {@link Serializable} to {@link String} according to
     * its {@link QName} type
     *
     * @param type  Alfresco {@link DataTypeDefinition#getName()}
     * @param value value to convert
     * @return converted value
     * @see DataTypeDefinition
     * @see QName
     */
    public String convert(QName type, Serializable value) {
        String result;
        String name = null;
        /* Check if type is not null and get its local name */
        if (type != null) {
            name = type.getLocalName();
        }
        /* Get action by type */
        Action action = actions.get(name);

        /* Execute action if it's found or call value#toString() */
        if (action != null) {
            result = action.execute(value);
        } else {
            result = value.toString();
        }
        return result;
    }
}
