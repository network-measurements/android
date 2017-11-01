package com.nawbar.networkmeasurements.logger;

/**
 * Created by Bartosz Nawrot on 2017-11-01.
 */

public interface LoggerInput {
    void initialize(String name);
    void log(String message);
}
