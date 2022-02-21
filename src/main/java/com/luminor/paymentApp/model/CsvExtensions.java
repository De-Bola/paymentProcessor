package com.luminor.paymentApp.model;

public enum CsvExtensions {
    VND_MSEXCEL("application/vnd.ms-excel"),
    TEXT_PLAIN("text/plain"),
    TEXT_CSV_1("text/csv"),
    TEXT_XCSV_1("text/x-csv"),
    APP_CSV("application/csv"),
    APP_XCSV("application/x-csv"),
    TEXT_CSV_2("text/comma-separated-values"),
    TEXT_XCSV_2("text/x-comma-separated-values");

    public final String mime;
    CsvExtensions(String mimeType) {
        this.mime = mimeType;
    }
}
