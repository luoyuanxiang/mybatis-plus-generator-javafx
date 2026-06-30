package com.example.generator.ui.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class UiLogAppender extends AppenderBase<ILoggingEvent> {

    private static UiLogAppender instance;
    private Consumer<String> logConsumer;

    public static synchronized void install(Consumer<String> consumer) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        if (instance != null) {
            instance.stop();
            Logger root = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
            root.detachAppender(instance);
        }
        instance = new UiLogAppender();
        instance.logConsumer = consumer;
        instance.setContext(context);
        instance.setName("UI_LOG");
        instance.start();
        Logger root = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.addAppender(instance);
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (logConsumer != null) {
            logConsumer.accept(eventObject.getFormattedMessage());
        }
    }
}
