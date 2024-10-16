package com.project.gymweb.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "exercises")
@Data
public class Exercise {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    private String name;
    private Long sets;
    private Long reps;

    @ManyToOne
    private Routine routine;
}