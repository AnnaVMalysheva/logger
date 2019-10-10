package com.training.logger.service.impl;

import com.training.logger.LoggerApplication;
import com.training.logger.dto.ErrorRecordDto;
import com.training.logger.entity.ErrorDate;
import com.training.logger.repository.ErrorDateRepository;
import com.training.logger.service.LoggerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoggerServiceImpl implements LoggerService {

    private static final String DATE_TIME_PATTERN =
            "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]).([0-9][0-9][0-9])).*";
    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final DateTimeFormatter fromFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
    private static final DateTimeFormatter toFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final String ERROR = "ERROR";
    private static final String LOG_EXTENSION = ".log";
    private static final String MINUTE = "minute";
    private Pattern pattern = Pattern.compile(DATE_TIME_PATTERN);

    private ErrorDateRepository errorDateRepository;
    private LoggerApplication.LogHolder logHolder;

    public LoggerServiceImpl(ErrorDateRepository errorDateRepository, LoggerApplication.LogHolder logHolder) {
        this.errorDateRepository = errorDateRepository;
        this.logHolder = logHolder;
    }

    @Override
    public void readErrorsFromFiles() throws IOException {
        Path dir = Paths.get(logHolder.getDirPath());
        List<ErrorDate> dates = Files.walk(dir)
                .filter(file -> file.toString().endsWith(LOG_EXTENSION))
                .parallel()
                .flatMap(p -> {
                    try {
                        return Files.lines(p);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }).filter(s -> s.contains(ERROR))
                .map(str -> {
                    Matcher matcher = pattern.matcher(str);
                    if(matcher.find()) {
                        String dateTime = matcher.group(1);
                        return ErrorDate.builder().date(LocalDateTime.parse(dateTime, inputFormatter)).build();
                    }
                    throw new RuntimeException("There is some strange line in your logs without date");
                }).collect(Collectors.toList());
        errorDateRepository.saveAll(dates);
    }

    @Override
    public void getErrorDistributionByInterval(String interval) {
        List<ErrorRecordDto> errorRecords = errorDateRepository.getByInterval(interval);
        writeToFile(errorRecords, interval);
    }

    private void writeToFile(List<ErrorRecordDto> errorRecords, String interval) {
            try(FileWriter fileWriter = new FileWriter("errorRecords.log");
                PrintWriter printWriter = new PrintWriter(fileWriter)) {
                errorRecords.forEach(record -> {
                    LocalDateTime recordDate = record.getRecordDate();
                    String date = fromFormatter.format(recordDate) + "-" + toFormatter.format(
                            MINUTE.equals(interval) ? recordDate.plusMinutes(1) : recordDate.plusHours(1));
                    printWriter.println(date + " Number of errors: " + record.getRecordCount());
                });
            } catch(IOException ioException) {
                log.error("There is some trouble with resource", ioException);
            }
    }
}