package com.luminor.paymentApp.service;

import com.luminor.paymentApp.exceptions.IllegalCountryCodeException;
import com.luminor.paymentApp.handlers.CsvHandler;
import com.luminor.paymentApp.model.Payment;
import com.luminor.paymentApp.model.PaymentDto;
import com.luminor.paymentApp.repository.PaymentRepository;
import com.luminor.paymentApp.service.mapper.PaymentMapper;
import fr.marcwrobel.jbanking.iban.Iban;
import fr.marcwrobel.jbanking.iban.IbanFormatException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository  paymentRepository;
    private final PaymentMapper mapper;

    public PaymentService(PaymentRepository repository, PaymentMapper paymentMapper){
        this.paymentRepository = repository;
        this.mapper = paymentMapper;
    }

    /* for singular payment entries */
    public Payment makePayment(PaymentDto paymentDto) throws IllegalCountryCodeException {
        Iban debtorIban = new Iban(paymentDto.getDebtorIban());
        if(CsvHandler.hasCountryCode(debtorIban.getCountryCode())){
            Payment payment = mapper.toModel(paymentDto);
            return paymentRepository.save(payment);
        }
        else throw new IllegalCountryCodeException("IBAN country code not allowed!");
    }

    public Page<Payment> getPayments(Pageable pageable){
        return paymentRepository.findAll(pageable);
    }

    /* for a list of payment entries */
    public Iterable<Payment> makePayments(List<PaymentDto> paymentDtos) {
        List<PaymentDto> paymentDtoList = new ArrayList<>();
        for (PaymentDto dto : paymentDtos) {
            try{
                Iban debtorIban = new Iban(dto.getDebtorIban());
                if (CsvHandler.hasCountryCode(debtorIban.getCountryCode())){
                    paymentDtoList.add(dto);
                }

            }catch (IbanFormatException ife){
                ife.printStackTrace();
            }
        }
        List<Payment> payment = mapper.toModels(paymentDtoList);
        return paymentRepository.saveAll(payment);
    }

    /* for a csv file formatted list of payment entries */
    public Iterable<Payment> makePayments(MultipartFile file) {
        try {
            List<Payment> paymentList = mapper.toModels(CsvHandler.csvToPayments(file.getInputStream()));
            return paymentRepository.saveAll(paymentList);
        } catch (IOException e) {
            throw new RuntimeException("csv data storage failure: " + e.getMessage());
        }
    }

    public Page<Payment> getPaymentsByDebtorIban(String iban, Pageable pageable) {
        return paymentRepository.getPaymentsByDebtorIban(iban, pageable);
    }
}
