package io.github.funkynoodles;

import java.time.LocalDate;

public class DateUtils {
    public static boolean isBetweenDatesInclusive(LocalDate date, LocalDate fromDate, LocalDate toDate) {
        return !date.isAfter(toDate) && !date.isBefore(fromDate);
    }
}
