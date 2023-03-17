package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

/**
 * Class implementing methods to validate inputs passed when the program is ran.
 */
public final class InputValidation {

    public static final DateValidator ISO8061 = new DateValidator("yyyy-MM-dd");
    public static final URL baseUrlBackup;

    //Store backup base url in case the one passed is invalid
    static {
        try {
            baseUrlBackup = new URL("https://ilp-rest.azurewebsites.net/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String minDateStr = "2023-01-01";
    private static final String maxDateStr = "2023-05-31";

    private InputValidation(){
    }

    /**
     * Runs the individual input validation algorithms for the arguments of the main method
     * @param strs Arguments passed in the main method, to check if they are valid
     * @return true if the input is valid according to the specification
     */
    public static Boolean isValidInput(String[] strs){
        if (!isValidLength(strs)){
            return false;
        }
        if (!isValidDate(strs[0])){
            return false;
        }
        return isValidBaseUrl(strs[1]);
    }

    /**
     * Checks length of arguments is 3
     * @param strs arguments from main method
     * @return true if exactly 3 arguments are passed, false otherwise
     */
    private static Boolean isValidLength(String[] strs){
        int len = strs.length;
        if (len != 3){
            System.err.println("Exactly 3 arguments need to be provided for the program to function. Provided number of arguments: " + strs.length);
            return false;
        }
        return true;
    }

    /**
     * Validates the date according to the ISO8061 format
     * @param dateStr date to validate
     * @return true if the date is in the correct format, false otherwise
     */
    private static Boolean isValidDate(String dateStr){
        try{
            Date date = ISO8061.stringToDate(dateStr);
            return isDateInRange(date);
        }
        catch (ParseException e){
            System.err.println("Please provide a date in ISO8061 (yyyy-MM-dd) format as the first argument");
            return false;
        }
    }

    /**
     * Validates the base url by trying to make a conenction to the url. It also appends a slash at the end if it
     * does not have one
     * @param baseUrlStr base url to validate
     * @return true if the url is valid, false otherwise
     */
    private static Boolean isValidBaseUrl(String baseUrlStr){
        try{
            baseUrlStr = appendSlashIfNeeded(baseUrlStr);
            URL baseUrl = new URL(baseUrlStr);
            return true;
        }
        catch (MalformedURLException e){
            System.err.println("Please provide a valid URL as the second argument");
            return false;
        }
    }


    /**
     * Checks that the date is within the given dates in the spec
     * @param date to check being within the range
     * @return true if the date is within the allowed dates.
     */
    private static Boolean isDateInRange(Date date){
        try {
            Date minDate = ISO8061.stringToDate(minDateStr);
            Date maxDate = ISO8061.stringToDate(maxDateStr);
            return date.after(minDate) && date.before(maxDate);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Add a slash at the end of a url if it does not have one
     * @param str of the url to add a slash if needed
     * @return the string after adding or not adding the slash.
     */
    public static String appendSlashIfNeeded(String str){
        if (!(str.endsWith("/"))){
            str += "/";
        }
        return str;
    }
}
