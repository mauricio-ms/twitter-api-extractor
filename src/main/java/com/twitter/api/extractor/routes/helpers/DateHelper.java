package com.twitter.api.extractor.routes.helpers;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateHelper {

    public String getDatePlusDays(final String date,
                                  final Integer days) {
        return LocalDate.parse(date)
                .plusDays(days)
                .format(DateTimeFormatter.ISO_DATE);
    }
}
