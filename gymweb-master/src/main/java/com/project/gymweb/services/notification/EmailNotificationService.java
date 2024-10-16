package com.project.gymweb.services.notification;

import com.project.gymweb.entities.Payment;
import com.project.gymweb.utils.PaymentObserver;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements PaymentObserver {
    private final EmailService emailService;

    public EmailNotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void update(Payment payment) {
        sendEmailNotification(payment);
    }

    private void sendEmailNotification(Payment payment) {
        var email = payment.getUser().getEmail();
        var subject = "GymWeb Payment Confirmed";
        var body = "Hello " + payment.getUser().getUsername() + ",\n\nYour payment of " + payment.getValue() + " has been successfully confirmed.";

        System.out.println("Sending email to " + email);
        System.out.println(subject + "\n" + body);

        emailService.sendEmail(email, subject, body);
    }
}
