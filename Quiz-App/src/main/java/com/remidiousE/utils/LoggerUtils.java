package com.remidiousE.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerUtils {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtils.class);

    public void logInfo(String message) {
        logger.info(message);
    }

    public void logWarning(String message) {
        logger.warn(message);
    }

    public void logError(String message, Throwable exception) {
        logger.error(message, exception);
    }
}
