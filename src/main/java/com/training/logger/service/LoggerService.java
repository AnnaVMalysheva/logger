package com.training.logger.service;

import java.io.IOException;

public interface LoggerService {

    void readErrorsFromFiles() throws IOException;

    void getErrorDistributionByInterval(String interval);
}