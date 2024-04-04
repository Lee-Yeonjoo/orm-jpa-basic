package jpa.exercise.domain;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    private LocalDate createDate;
    private LocalDate updateDate;
}
