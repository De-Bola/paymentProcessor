package com.luminor.paymentApp.controllers;

import com.luminor.paymentApp.exceptions.IllegalCountryCodeException;
import com.luminor.paymentApp.handlers.CsvHandler;
import com.luminor.paymentApp.model.Payment;
import com.luminor.paymentApp.model.PaymentDao;
import com.luminor.paymentApp.model.PaymentDto;
import com.luminor.paymentApp.service.PaymentService;
import fr.marcwrobel.jbanking.iban.IbanFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:8089"})
@RequestMapping("/api")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    // for making singular payments, checks for countrycode and validity of Iban
    // returns status code 201 when all goes well
    @PostMapping(value = "/payments")
    public ResponseEntity<?> makePayment(@RequestBody PaymentDto paymentDto) {

        try {
            if(paymentService.makePayment(paymentDto) != null){
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } catch (IllegalCountryCodeException | IbanFormatException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // for making payments via json lists, checks for countrycode and validity of Iban
    // ibans without the allowed countrycodes are ignored
    // invalid ibans aren't persisted, but valid entries will be persisted
    // returns 201 and number of valid entries when all goes well
    @PostMapping(value = "/list/payments")
    public ResponseEntity<?> makePayments(@RequestBody List<PaymentDto> payments) {
        if (payments != null) {
            Iterable<Payment> persistedPayments = paymentService.makePayments(payments);
            Integer sz = ((Collection<?>) persistedPayments).size();
            String response = sz + " valid entries were found & saved!";
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // for making payments via csv file uploads, checks for countrycode and validity of Iban
    // ibans without the allowed countrycodes are ignored
    // invalid ibans aren't persisted, but valid entries will be persisted
    // returns 201 and number of valid entries when all goes well
    @PostMapping(value = "/payment-files", consumes = "multipart/form-data")
    public ResponseEntity<?> makePayments(@RequestParam("file") MultipartFile payments) {
        if (CsvHandler.hasCSVFormat(payments)){
            try {
                Iterable<Payment> persistedPayments = paymentService.makePayments(payments);
                Integer sz = ((Collection<?>) persistedPayments).size();
                String response = sz + " valid entries were found & saved!";
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // for getting all payments either via a certain iban or all entries altogether
    // returns 200 and all found entries when all goes well
    @GetMapping(value = "/payments")
    public ResponseEntity<?> getPayments(
            @RequestParam(required = false) String debtorIban,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentDao> payments;
        try{
            if (debtorIban == null){
                payments = paymentService.getPayments(pageable);
            }
            else{
                payments = paymentService.getPaymentsByDebtorIban(debtorIban, pageable);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("Payments", payments.getContent());
            response.put("Current_Page", payments.getNumber());
            response.put("Payments_Count", payments.getTotalElements());
            response.put("Pages_Count", payments.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/payments-persisted")
    public ResponseEntity<?> getPersistedPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Map<String, Object> response = new HashMap<>();
        Page<Payment> payments;
        try{
            payments = paymentService.getOriginalPayments(pageable);
            response.put("Payments", payments.getContent());
            response.put("Current_Page", payments.getNumber());
            response.put("Payments_Count", payments.getTotalElements());
            response.put("Pages_Count", payments.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
