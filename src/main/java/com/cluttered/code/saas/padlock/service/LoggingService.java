package com.cluttered.code.saas.padlock.service;

import com.cluttered.code.saas.padlock.config.LoggingConfig;
import com.google.inject.Inject;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.service.Service;
import ratpack.service.StartEvent;

/**
 * @author cluttered.code@gmail.com
 */
public class LoggingService implements Service {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingService.class);

    private final LoggingConfig loggingConfig;

    @Inject
    private LoggingService(final LoggingConfig loggingConfig) {
        this.loggingConfig = loggingConfig;
    }

    @Override
    public void onStart(final StartEvent event) {
        setLoggingLevel(loggingConfig.getLevel());
    }

    private void setLoggingLevel(final Level level) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        final LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        LOG.info("setting log level to '{}'", level);
        loggerConfig.setLevel(level);
        ctx.updateLoggers();
    }
}
