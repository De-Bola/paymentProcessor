package com.luminor.paymentApp.service;

import com.luminor.paymentApp.repository.LocationRepository;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@Component
public class LocationService implements LocationRepository {
    @Override
    public String getCountryInfo(String ipAddress) {
        String db = "src/main/resources/static/location/GeoLite2-Country.mmdb";
        try {
            File file = new File(db);
            DatabaseReader databaseReader = new DatabaseReader.Builder(file).build();
            CountryResponse response = databaseReader.country(InetAddress.getByName(ipAddress));
            return response.getCountry().getIsoCode() + ": " + response.getCountry().getName();
        } catch (IOException | GeoIp2Exception e) {
            e.printStackTrace();
        }
        return "could not resolve location.";
    }
}
