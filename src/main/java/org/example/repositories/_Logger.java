package org.example.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public interface _Logger<T> {
    final Logger LOGGER = LogManager.getLogger(_Logger.class);
    public default void logInfo(T message){
        LOGGER.info(message);
    }
    public default void logWarn(T message){
        LOGGER.warn(message);
    }
    public default void logError(SQLException e){
        LOGGER.error("%s - %s".formatted(e.getMessage(), e.getStackTrace()));
    }

}
