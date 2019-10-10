package com.training.logger.service;

import com.training.logger.LoggerApplication;
import com.training.logger.entity.ErrorDate;
import com.training.logger.repository.ErrorDateRepository;
import com.training.logger.service.impl.LoggerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class LoggerServiceImplTest {
    public static final String FILE_NAME = "course_test";

    @Mock
    private ErrorDateRepository errorDateRepository;

    @Mock
    private LoggerApplication.LogHolder logHolder ;

    @InjectMocks
    private LoggerServiceImpl loggerService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(logHolder.getDirPath()).thenReturn("src/test/resources/files");
    }

    @Test
    public void testReadErrorsFromFiles() throws IOException {

        when(errorDateRepository.save(any(ErrorDate.class))).thenReturn(new ErrorDate());
        loggerService.readErrorsFromFiles();
        verify(errorDateRepository, times(1)).save(any(ErrorDate.class));

    }

    @Test
    public void testGetErrorDistributionByInterval() throws IOException {

        when(errorDateRepository.save(any(ErrorDate.class))).thenReturn(new ErrorDate());
        loggerService.readErrorsFromFiles();
        verify(errorDateRepository, times(1)).save(any(ErrorDate.class));

    }


}
