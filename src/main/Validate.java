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
        boolean isValid = false;

        // Use the libphonenumber library to validate Number
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber swissNumberProto = null;
        try {
            swissNumberProto = phoneUtil.parse(phoneNumber, "CH");
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }

        if (phoneUtil.isValidNumber(swissNumberProto)) {
            isValid = true;
        }

        // The Library failed to validate number if it contains - sign
        // thus use regex to validate Mobile Number.
        String regex = "[0-9*#+() -]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}