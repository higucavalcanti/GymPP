package com.project.gymweb.utils;

import com.project.gymweb.entities.Payment;

public interface PaymentObserver {
    void update(Payment payment);
}
