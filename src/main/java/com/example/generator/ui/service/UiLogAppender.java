package com.example.generator.ui.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * Logback 自定义 Appender，将 SLF4J 日志转发至 JavaFX UI 文本区域。
 * <p>
 * 通过 {@link #install(Consumer)} 注册到 Root Logger，生成代码时
 * {@link GeneratorService} 的日志会实时显示在界面日志面板中。
 * </p>
 */
public class UiLogAppender extends AppenderBase<ILoggingEvent> {

    /** 单例实例，重复 install 时会先卸载旧实例。 */
    private static UiLogAppender instance;

    /** 日志消息消费者，通常绑定到 UI TextArea 的 append 操作。 */
    private Consumer<String> logConsumer;

    /**
     * 安装 UI 日志 Appender 到 Logback Root Logger。
     *
     * @param consumer 接收格式化日志消息的回调，null 时忽略输出
     */
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
