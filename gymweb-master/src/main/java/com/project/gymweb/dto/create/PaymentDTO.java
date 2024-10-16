package com.project.gymweb.dto.create;

import com.project.gymweb.enums.PaymentType;

import java.util.Date;
import java.util.UUID;

public record PaymentDTO(Double value, Date date, PaymentType type, UUID userId) {
}
