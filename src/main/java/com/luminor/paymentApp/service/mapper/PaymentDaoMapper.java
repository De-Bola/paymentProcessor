package com.luminor.paymentApp.service.mapper;

import com.luminor.paymentApp.model.Payment;
import com.luminor.paymentApp.model.PaymentDao;
import com.luminor.paymentApp.model.PaymentDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PaymentDaoMapper extends MapperInterface<Payment, PaymentDao> {
}