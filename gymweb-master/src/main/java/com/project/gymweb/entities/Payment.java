package com.project.gymweb.entities;

import com.project.gymweb.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table
@Data
public class Payment {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    private Double value;
    private Date date;
    private PaymentType type;
    @ManyToOne
    private User user;
}
