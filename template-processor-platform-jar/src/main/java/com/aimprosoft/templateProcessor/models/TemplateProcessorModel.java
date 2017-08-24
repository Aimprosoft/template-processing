package com.aimprosoft.templateProcessor.models;

import org.alfresco.service.namespace.QName;

/**
 * Container with constants
 */
public final class TemplateProcessorModel {
    public final static String NAMESPACE_URI = "http://www.aimprosoft.com/model/content/1.0";

    public final static QName ASPECT_TEMPLATE = QName.createQName(NAMESPACE_URI, "template");
    public final static QName PROP_PROCESSED = QName.createQName(NAMESPACE_URI, "processed");

    private TemplateProcessorModel(){}
}
