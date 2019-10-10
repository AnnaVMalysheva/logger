package com.training.logger.dto;

import java.time.LocalDateTime;

public interface ErrorRecordDto {

    LocalDateTime getRecordDate();
    Integer getRecordCount();
}