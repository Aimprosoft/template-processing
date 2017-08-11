package com.aimprosoft.templateProcessor.models;

import org.alfresco.service.namespace.QName;

/**
 * Container with constants
 */
public final class TemplateProcessorModel {
    public final static String NAMESPACE_URI = "http://www.aimprosoft.com/model/content/1.0";
    public final static String PROPERTY_LOCAL_NAME = "processed";

    public final static QName ASPECT_PROPERTY = QName.createQName(NAMESPACE_URI, PROPERTY_LOCAL_NAME);

    private TemplateProcessorModel(){}
}
