package com.training.logger.repository;

import com.training.logger.dto.ErrorRecordDto;
import com.training.logger.entity.ErrorDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorDateRepository extends JpaRepository<ErrorDate, Long> {

    @Query(value = "select date_trunc(:interval, date) as recordDate, count(1) as recordCount from error_date " +
            "group by 1 order by 1",
            nativeQuery = true)
    List<ErrorRecordDto> getByInterval(@Param("interval") String interval);
}