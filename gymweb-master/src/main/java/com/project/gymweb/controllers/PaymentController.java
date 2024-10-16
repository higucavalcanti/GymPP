package com.project.gymweb.controllers;

import com.project.gymweb.dto.create.PaymentDTO;
import com.project.gymweb.dto.view.PaymentRO;
import com.project.gymweb.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController("Payments")
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/")
    public ResponseEntity<List<PaymentRO>> payments() {
        var payments = paymentService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentRO>> paymentsByUserId(@PathVariable("userId") UUID userId) {
        var payments = paymentService.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/user/username/{username}")
    public ResponseEntity<List<PaymentRO>> paymentsByUserUsername(@PathVariable String username) {
        var payments = paymentService.findAllByUserUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/user/email/{email}")
    public ResponseEntity<List<PaymentRO>> paymentsByUserEmail(@PathVariable String email) {
        var payments = paymentService.findAllByUserEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentRO> payment(@PathVariable UUID id) {
        var payment = paymentService.getPayment(id);
        return ResponseEntity.status(HttpStatus.OK).body(payment);
    }

    @PostMapping("/")
    public ResponseEntity<PaymentRO> addPayment(@RequestBody PaymentDTO paymentDTO) {
        var payment = paymentService.createPayment(paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentRO> updatePayment(@PathVariable UUID id, @RequestBody PaymentDTO paymentDTO) {
        var routine = paymentService.updatePayment(id, paymentDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(routine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable UUID id) {
        paymentService.deletePayment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
