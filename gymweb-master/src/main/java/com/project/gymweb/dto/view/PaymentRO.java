package com.project.gymweb.dto.view;

import java.util.Date;
import java.util.UUID;

public record PaymentRO(UUID id, Double value, Date date, com.project.gymweb.enums.PaymentType type, UUID userId) {
}
