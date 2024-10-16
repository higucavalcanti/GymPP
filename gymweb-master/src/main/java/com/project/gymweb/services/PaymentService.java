package com.project.gymweb.services;

import com.project.gymweb.dto.create.PaymentDTO;
import com.project.gymweb.dto.view.PaymentRO;
import com.project.gymweb.entities.Payment;
import com.project.gymweb.enums.PaymentType;
import com.project.gymweb.exceptions.PaymentNotFoundException;
import com.project.gymweb.exceptions.UserNotFoundException;
import com.project.gymweb.repositories.PaymentRepository;
import com.project.gymweb.repositories.UserRepository;
import com.project.gymweb.services.discount.AnnualDiscountStrategy;
import com.project.gymweb.services.discount.NoDiscountStrategy;
import com.project.gymweb.services.discount.SemiannualDiscountStrategy;
import com.project.gymweb.services.notification.EmailNotificationService;
import com.project.gymweb.utils.DiscountStrategy;
import com.project.gymweb.utils.PaymentObserver;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository repository;
    private final UserRepository userRepository;
    private final EmailNotificationService emailNotificationService;
    private final List<PaymentObserver> observers = new ArrayList<>();
    @Setter
    private DiscountStrategy discountStrategy;

    @Autowired
    public PaymentService(PaymentRepository repository, UserRepository userRepository, EmailNotificationService emailNotificationService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.emailNotificationService = emailNotificationService;

        addObserver(emailNotificationService);
    }

    public List<PaymentRO> findAll() {
        return repository.findAll().stream().map(this::entityToRO).toList();
    }

    public List<PaymentRO> findByUserId(UUID userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " was not found"));
        return repository.findAllByUserId(user.getId()).stream().map(this::entityToRO).toList();
    }

    public List<PaymentRO> findAllByUserUsername(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with name " + username + " was not found"));
        return repository.findAllByUserId(user.getId()).stream().map(this::entityToRO).toList();
    }

    public List<PaymentRO> findAllByUserEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " was not found"));
        return repository.findAllByUserId(user.getId()).stream().map(this::entityToRO).toList();
    }

    public PaymentRO getPayment(UUID paymentId) {
        var payment = repository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException("Payment with id " + paymentId + " was not found"));
        return entityToRO(payment);
    }

    public PaymentRO createPayment(PaymentDTO paymentDTO) {
        Payment payment = dtoToEntity(paymentDTO);

        payment.setValue(discountApplier(payment));

        notifyObservers(payment);

        var savedPayment = repository.save(payment);

        return entityToRO(savedPayment);
    }

    public PaymentRO updatePayment(UUID id, PaymentDTO paymentDTO) {
        var payment = repository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment with id " + id + " was not found"));
        var user = userRepository.findById(paymentDTO.userId()).orElseThrow(() -> new UserNotFoundException("User with id " + id + " was not found"));

        payment.setValue(paymentDTO.value());
        payment.setDate(paymentDTO.date());
        payment.setType(paymentDTO.type());
        payment.setUser(user);

        payment.setValue(discountApplier(payment));

        var savedPayment = repository.save(payment);

        return entityToRO(savedPayment);
    }

    public void deletePayment(UUID id) {
        var payment = repository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment with id " + id + " was not found"));
        repository.deleteById(payment.getId());
    }

    public void addObserver(PaymentObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(PaymentObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Payment payment) {
        for (PaymentObserver observer : observers) {
            observer.update(payment);
        }
    }

    private Payment dtoToEntity(PaymentDTO paymentDTO) {
        Payment payment = new Payment();

        payment.setId(UUID.randomUUID());
        payment.setValue(paymentDTO.value());
        payment.setDate(paymentDTO.date());
        payment.setType(paymentDTO.type());

        var user = userRepository.findById(paymentDTO.userId()).orElseThrow(() -> new UserNotFoundException("User with id " + paymentDTO.userId() + " was not found"));
        payment.setUser(user);

        return payment;
    }

    private PaymentRO entityToRO(Payment payment) {
        return new PaymentRO(payment.getId(), payment.getValue(), payment.getDate(), payment.getType(), payment.getUser().getId());
    }

    private Double discountApplier(Payment payment) {
        switch (payment.getType()) {
            case PaymentType.ANNUAL:
                this.discountStrategy = new AnnualDiscountStrategy();
                break;
            case PaymentType.SEMIANNUAL:
                this.discountStrategy = new SemiannualDiscountStrategy();
                break;
            default:
                this.discountStrategy = new NoDiscountStrategy();
        }

        return discountStrategy.apllyDiscount(payment.getValue());
    }
}
