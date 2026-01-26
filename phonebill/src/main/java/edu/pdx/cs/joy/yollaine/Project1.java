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
 * The main class for the Phone Bill Project
 */
public class Project1 {

  /**
  * Determines if a date and time is valid
  * @param dateAndTime
  *      The data and time combo to validate
  * @return
  *      Returns true if dataAndTime is valid, false if it isn't
  */
  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    Pattern p = Pattern.compile("^([0-9]|[0-9]{2})/([0-9]|[0-9]{2})/[0-9]{4} [0-9]{2}:[0-9]{2}$");
    Matcher m = p.matcher(dateAndTime);
    return m.find();
  }

  @VisibleForTesting
  static boolean isValidPhoneNumber(String number) {
    Pattern p = Pattern.compile("^(\\+([0-9]){1,3} )?([0-9]{3}-){2}[0-9]{4}$");
    Matcher m = p.matcher(number);
    return m.find();
  }

  /**
  * The main method of Project1
  * @param args
  *      The command line arguments fed into the program
  */
  public static void main(String[] args) {
    ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));

    boolean printFlag = false;
    boolean readMeFlag = false;
    boolean unknownFlag = false;
    String currentArg;
    try {
      while ((currentArg = argList.getFirst()).startsWith("-")) {
        if (currentArg.contains("print")) {
        if (!printFlag) {
          printFlag = true;
        }
        argList.removeFirst();
        continue;
        }
        if (currentArg.contains("README")) {
          readMeFlag = true;

          // Break to immediately print out the README and exit
          // Doesn't need to process other arguments
          break;
        }
        unknownFlag = true;
        break;
      }
    } catch (NoSuchElementException e) {
      System.err.println("Missing phone bill information");
      return;
    }

    if (unknownFlag) {
      System.err.println("Unknown option " + currentArg);
      return;
    }

    if (readMeFlag) {
      try (InputStream readme = Project1.class.getResourceAsStream("README.txt")
    ) {
      if (readme == null) {
        throw new IOException("Unable to access README.txt");
      }
      StringBuffer readMeOutput = new StringBuffer();
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line;

      while ((line = reader.readLine()) != null) {
        readMeOutput.append(line);
        readMeOutput.append('\n');
      }
      System.out.println(readMeOutput);
      return;
    } catch (IOException e) {
      System.err.println("Unable to access README.txt");
      return;
      }
    }

    // check if there's too many command line arguments
    if (argList.size() > 5) {
      System.err.println("Too many command line arguments");
      System.err.println("usage: java -jar target/phonebill-1.0.0.jar [options] customer callerNumber calleeNumber beginTime endTime");
      return;
    }


    String name = argList.getFirst();
    String callerNum;
    String calleeNum;
    String beginTime;
    String endTime;
    try {
      callerNum = argList.get(1);
      calleeNum = argList.get(2);
      beginTime = argList.get(3);
      endTime = argList.get(4);
    } catch (IndexOutOfBoundsException e) {
      if (e.getMessage().contains("Index 1")) {
        System.err.println("Missing Caller Number");
      } else if (e.getMessage().contains("Index 2")) {
        System.err.println("Missing Callee Number");
      } else if (e.getMessage().contains("Index 3")) {
        System.err.println("Missing Begin Time");
      } else if (e.getMessage().contains("Index 4")) {
        System.err.println("Missing End Time");
      }
      return;
    }

    if (!isValidPhoneNumber(callerNum)) {
      System.err.println("Invalid Caller Phone Format. Phone format should be +[Country Code] ###-###-####. Country code is optional.");
      return;
    } else if (!isValidPhoneNumber(calleeNum)) {
      System.err.println("Invalid Callee Phone Format. Phone format should be +[Country Code] ###-###-####. Country code is optional.");
      return;
    } else if (!isValidDateAndTime(beginTime)) {
      System.err.println("Invalid Begin Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm");
      return;
    } else if (!isValidDateAndTime(endTime)) {
      System.err.println("Invalid End Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm");
      return;
    }


    PhoneCall newCall = new PhoneCall(callerNum, calleeNum, beginTime, endTime);
    PhoneBill newBill = new PhoneBill(name);

    newBill.addPhoneCall(newCall);

    if (printFlag) {
      System.out.println(newBill);
      for (PhoneCall call: newBill.getPhoneCalls()) {
        System.out.println(call);
      }
    }
  }

}