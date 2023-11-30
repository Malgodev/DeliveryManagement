package main;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {
    public static boolean isValidEmail(String email) {
        if(email == null) return true;
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public boolean isMobileNumberValid(String phoneNumber) {

        String regex = "^[0-9\\-\\+]{10}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        if (matcher.matches()) {
           return true;
        }
        return false;
    }

    public boolean isZipValid(String zip) {

        String regex = "^[0-9\\-\\+]{5}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(zip);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}