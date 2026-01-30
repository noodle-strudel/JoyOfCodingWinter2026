package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;

import java.io.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class for Project 2
 */
public class Project2 {

    /**
     * Determines if a date and time is valid.
     * @param dateAndTime
     *      The data and time combo to validate.
     * @return
     *      Returns true if dataAndTime is valid and false if it's invalid.
     */
    @VisibleForTesting
    static boolean isValidDateAndTime(String dateAndTime) {
        return Project1.isValidDateAndTime(dateAndTime);
    }

    /**
     * Determines if a phone number is in a valid format.
     * @param number
     *      The phone number to validate.
     * @return
     *      Returns true if number is valid.
     */
    @VisibleForTesting
    static boolean isValidPhoneNumber(String number) {
        return Project1.isValidPhoneNumber(number);
    }

    public static void main(String[] args) {

    }
}
