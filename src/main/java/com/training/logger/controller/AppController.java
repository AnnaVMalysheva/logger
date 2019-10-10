package com.training.logger.controller;

import com.training.logger.service.LoggerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.training.logger.service.impl.LoggerServiceImpl.MINUTE;

@RestController
@AllArgsConstructor
@CrossOrigin
public class AppController {

    private LoggerService loggerService;

    @GetMapping("/parse")
    public void readFiles() throws IOException {
        loggerService.readErrorsFromFiles();
    }

    @GetMapping("/distribution")
    public void getErrorDistribution(@RequestParam(defaultValue = MINUTE) String distributionInterval) {
        loggerService.getErrorDistributionByInterval(distributionInterval);
    }
}