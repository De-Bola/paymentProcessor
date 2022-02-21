package com.luminor.paymentApp.handlers;

import com.luminor.paymentApp.repository.PaymentRepository;
import com.luminor.paymentApp.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LocationHandler implements HandlerInterceptor {

    @Autowired
    private PaymentService service;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {


        String ipAddress = request.getHeader("X-FORWARDED-FOR");

        if(ipAddress== null) {

            ipAddress = request.getRemoteAddr();
        }

        service.storeIpAddress(ipAddress);
        return true;
    }
}