package jpa.exercise.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BOOK")
public class Book extends Item{

    private String author;
    private String isbn;
}
