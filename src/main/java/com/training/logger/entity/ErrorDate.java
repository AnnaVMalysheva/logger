package com.training.logger.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "error_date_ids_gen")
    @SequenceGenerator(name = "error_date_ids_gen", sequenceName = "error_date_ids_seq", allocationSize = 1)
    private Long id;

    private LocalDateTime date;
}
