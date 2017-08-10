package com.aimprosoft.templateProcessor.exceptions;

/**
 * Exception thrown when errors occurred in {@code TemplateProcessorService}
 */
public class TemplateProcessingException extends Exception {

    public TemplateProcessingException(String s){super(s);}
    public TemplateProcessingException(String s, Throwable o){super(s, o);}
}
