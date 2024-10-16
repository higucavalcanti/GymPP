package com.project.gymweb.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Table
@Data
public class Routine {
    @Id
    private UUID id;
    private String name;
    @ManyToOne
    private User user;
}
