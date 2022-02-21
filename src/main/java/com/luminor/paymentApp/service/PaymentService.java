package com.luminor.paymentApp.service;

import com.luminor.paymentApp.exceptions.IllegalCountryCodeException;
import com.luminor.paymentApp.handlers.CsvHandler;
import com.luminor.paymentApp.model.Payment;
import com.luminor.paymentApp.model.PaymentDao;
import com.luminor.paymentApp.model.PaymentDto;
import com.luminor.paymentApp.repository.PaymentRepository;
import com.luminor.paymentApp.service.mapper.PaymentDaoMapper;
import com.luminor.paymentApp.service.mapper.PaymentDtoMapper;
import fr.marcwrobel.jbanking.iban.Iban;
import fr.marcwrobel.jbanking.iban.IbanFormatException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository  paymentRepository;
    private final PaymentDtoMapper mapper;
    private final PaymentDaoMapper daoMapper;
    private String ipLocation = new String();

    public PaymentService(PaymentRepository repository, PaymentDtoMapper paymentDtoMapper, PaymentDaoMapper paymentDaoMapper){
        this.paymentRepository = repository;
        this.mapper = paymentDtoMapper;
        this.daoMapper = paymentDaoMapper;
    }

    /* for singular payment entries */
    public Payment makePayment(PaymentDto paymentDto) throws IllegalCountryCodeException {
        Iban debtorIban = new Iban(paymentDto.getDebtorIban());
        if(CsvHandler.hasCountryCode(debtorIban.getCountryCode())){
            Payment payment = mapper.toModel(paymentDto);
            payment.setLocation(getIpLocation());
            return paymentRepository.save(payment);
        }
        else throw new IllegalCountryCodeException("IBAN country code not allowed!");
    }

    public Page<PaymentDao> getPayments(Pageable pageable){
        Page<Payment> list = paymentRepository.findAll(pageable);
        List<PaymentDao> daoList = daoMapper.toDtos(list.toList());
        return new PageImpl<>(daoList, pageable, list.getTotalElements());
    }

    public Page<Payment> getOriginalPayments(Pageable pageable){
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
        List<Payment> payments = mapper.toModels(paymentDtoList);
        for (Payment obj : payments) {
            obj.setLocation(getIpLocation());
        }
        return paymentRepository.saveAll(payments);
    }

    /* for a csv file formatted list of payment entries */
    public Iterable<Payment> makePayments(MultipartFile file) {
        try {
            List<Payment> paymentList = mapper.toModels(CsvHandler.csvToPayments(file.getInputStream()));
            for (Payment obj : paymentList) {
                obj.setLocation(getIpLocation());
            }
            return paymentRepository.saveAll(paymentList);
        } catch (IOException e) {
            throw new RuntimeException("csv data storage failure: " + e.getMessage());
        }
    }

    public Page<PaymentDao> getPaymentsByDebtorIban(String iban, Pageable pageable) {
        Page<Payment> list = paymentRepository.getPaymentsByDebtorIban(iban, pageable);
        List<PaymentDao> daoList = daoMapper.toDtos(list.toList());
        return new PageImpl<>(daoList, pageable, daoList.size());
    }

    public void storeIpAddress(String ipAddress) {
        this.ipLocation = ipAddress;
    }

    public String getIpLocation() {
        return this.ipLocation;
    }
}
