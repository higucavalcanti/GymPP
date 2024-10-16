package com.project.gymweb.services.notification;

import com.project.gymweb.entities.Payment;
import com.project.gymweb.utils.PaymentObserver;
import org.springframework.stereotype.Service;

@Service
public class AdminNotificationService implements PaymentObserver {
    @Override
    public void update(Payment payment) {
        System.out.println("Sending email to admins about payment: " + payment.getId());
    }
}
