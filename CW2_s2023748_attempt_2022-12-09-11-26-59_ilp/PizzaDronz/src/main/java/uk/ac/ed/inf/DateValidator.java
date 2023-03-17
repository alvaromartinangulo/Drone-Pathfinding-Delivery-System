package uk.ac.ed.inf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class used to validate and switch between dates and strings.
 */
public class DateValidator {
    private final SimpleDateFormat sdf;

    public DateValidator(String dateFormat){
        this.sdf = new SimpleDateFormat(dateFormat);
    }

    /**
     *
     * @param date as a String to parse into a date with the sdf format of the current DateValidator
     * @return the date in Date format
     * @throws ParseException if the date is not in the sdf of the current DateValidator
     */
    public Date stringToDate(String date) throws ParseException {
        return this.sdf.parse(date);
    }

    /**
     *
     * @param date date to format into a string of the current sdf format of this DateValidator
     * @return the date in the sdf format as a string.
     */
    public String dateToString(Date date){
        return this.sdf.format(date);
    }
}
