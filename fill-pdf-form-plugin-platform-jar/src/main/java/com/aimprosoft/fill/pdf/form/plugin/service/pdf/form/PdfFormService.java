package com.aimprosoft.fill.pdf.form.plugin.service.pdf.form;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.pdfbox.exceptions.COSVisitorException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

public interface PdfFormService {

    void fillPdfFormFromMetaData(NodeRef nodeRef, Map<QName,Serializable> propertyMap) throws IOException, COSVisitorException;

}
