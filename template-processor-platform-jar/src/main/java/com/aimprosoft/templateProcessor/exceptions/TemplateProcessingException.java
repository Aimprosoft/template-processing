package com.aimprosoft.templateProcessor.exceptions;

/**
 * Common exception {@code TemplateProcessingException}
 * thrown when system processes templates
 */
public class TemplateProcessingException extends Exception {

    public TemplateProcessingException(String s){super(s);}
    public TemplateProcessingException(String s, Throwable o){super(s, o);}
}
