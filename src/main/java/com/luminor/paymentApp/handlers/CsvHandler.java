package com.luminor.paymentApp.handlers;

import com.luminor.paymentApp.model.CsvExtensions;
import com.luminor.paymentApp.model.IbanCountryCodes;
import com.luminor.paymentApp.model.PaymentDto;
import com.luminor.paymentApp.service.PaymentService;
import fr.marcwrobel.jbanking.iban.Iban;
import fr.marcwrobel.jbanking.iban.IbanFormatException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvHandler {

    public static boolean hasCSVFormat(MultipartFile file) {
        for (CsvExtensions extension :
                CsvExtensions.values()) {
            if (extension.mime.equalsIgnoreCase(file.getContentType())){
                return true;
            }
        }
        return false;
    }

    public static boolean hasCountryCode(String countryCode) {
        for (IbanCountryCodes code :
                IbanCountryCodes.values()) {
            if (code.name().equalsIgnoreCase(countryCode)){
                return true;
            }
        }
        return false;
    }

    public static List<PaymentDto> csvToPayments(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(
                     fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())
             ) {
            List<PaymentDto> paymentDtos = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                try{
                    Iban debtorIban = new Iban(csvRecord.get("debtorIban"));
                    if (hasCountryCode(debtorIban.getCountryCode())){
                        PaymentDto paymentDto = new PaymentDto(csvRecord.get("amount"), debtorIban.toPrintableString());
                        paymentDtos.add(paymentDto);
                    }
                } catch (IbanFormatException ife){
                    ife.printStackTrace();
                }
            }
            return paymentDtos;
        } catch (IOException e) {
            throw new RuntimeException("CSV parse failure: " + e.getMessage());
        }
    }
}
