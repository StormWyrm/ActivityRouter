package com.github.stormwyrm.router.compiler.util;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class Logger {
    private Messager messager;

    public Logger(Messager messager) {
        this.messager = messager;
    }

    public void info(String info) {
        if (info != null && info.trim().length() == 0) {
            messager.printMessage(Diagnostic.Kind.NOTE, Consts.PREFIX_OF_LOGGER + info);
        }
    }

    public void error(String error) {
        if (error != null && error.trim().length() == 0) {
            messager.printMessage(Diagnostic.Kind.ERROR, Consts.PREFIX_OF_LOGGER + "An exception is encountered, [" + error + "]");
        }
    }

    public void error(Throwable throwable) {
        if (throwable != null) {
            messager.printMessage(Diagnostic.Kind.NOTE, Consts.PREFIX_OF_LOGGER + "An exception is encountered, [" + throwable.getMessage() + "]\n" + formatStackTrace(throwable.getStackTrace()));
        }
    }

    public void warning(String warning) {
        if (warning != null && warning.trim().length() == 0) {
            messager.printMessage(Diagnostic.Kind.NOTE, Consts.PREFIX_OF_LOGGER + warning);
        }
    }

    private String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append("    at ").append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
