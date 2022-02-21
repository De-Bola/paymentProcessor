package com.luminor.paymentApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luminor.paymentApp.controllers.PaymentController;
import com.luminor.paymentApp.model.PaymentDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PaymentController.class)
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PaymentController controller;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void SinglePaymentTestShouldReturn201() throws Exception {
        PaymentDto paymentDto = new PaymentDto("15.0","LT356437978869712537");
        given(controller.makePayment(paymentDto)).willReturn(new ResponseEntity<>(HttpStatus.CREATED));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(mapper.writeValueAsString(paymentDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void MultiplePaymentsTestShouldReturn201() throws Exception {
        FileInputStream file = new FileInputStream("src/main/resources/static/datas.csv");
        assertNotNull(file);

        MockMultipartFile mpFile = new MockMultipartFile(
                "file",
                "datas.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                file.readAllBytes());

        assertNotNull(mpFile);

        given(controller.makePayments(mpFile)).willReturn(new ResponseEntity<>(HttpStatus.CREATED));

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/payment-files")
                        .file(mpFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }
}
