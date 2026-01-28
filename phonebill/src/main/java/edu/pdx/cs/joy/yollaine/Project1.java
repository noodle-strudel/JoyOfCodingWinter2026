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
 * The main class for the Phone Bill Project.
 */
public class Project1 {

  /**
  * Determines if a date and time is valid.
  * @param dateAndTime
  *      The data and time combo to validate.
  * @return
  *      Returns true if dataAndTime is valid and false if it's invalid.
  */
  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    Pattern p = Pattern.compile("^([0-9]|[0-9]{2})/([0-9]|[0-9]{2})/[0-9]{4} [0-9]{2}:[0-9]{2}$");
    Matcher m = p.matcher(dateAndTime);
    return m.find();
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
    Pattern p = Pattern.compile("^([0-9]{3}-){2}[0-9]{4}$");
    Matcher m = p.matcher(number);
    return m.find();
  }

  /**
  * The main method of Project1.
  * @param args
  *      The command line arguments fed into the program.
  */
  public static void main(String[] args) {
    // Transform args into an ArrayList for easier manipulation
    ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));

    // Process options until a non-option is encountered
    boolean printFlag = false;
    boolean readMeFlag = false;
    boolean unknownArgFlag = false;
    String currentArg = "";
    String unknownArg = "";
    try {
      while ((currentArg = argList.getFirst()).startsWith("-")) {
        if (currentArg.contains("print")) {
          printFlag = true;
        } else if (currentArg.contains("README")) {
          readMeFlag = true;
        } else {
          unknownArgFlag = true;
          unknownArg = currentArg;
        }
        argList.removeFirst();
      }
    } catch (NoSuchElementException e) {
      // If there's no -README flag and no unknown arguments, then the program will try to
      // store phone bill information without exiting.
      // But since there's no arguments and only options, it will give an error message.
      if (!readMeFlag && !unknownArgFlag) {
        System.err.println("Missing phone bill information");
        System.err.println("usage: java -jar target/phonebill-1.0.0.jar [options] customer callerNumber calleeNumber beginDate beginTime endDate endTime");
        return;
      }
    }

    // Print out the README and exit the program
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

    // Print an error if an unknown argument is found and exit
    if (unknownArgFlag) {
      System.err.println("Unknown option " + unknownArg);
      System.err.println("options are (options may appear in any order before arguments):");
      System.err.println("\t-print          Prints a description of the new phone call");
      System.err.println("\t-README         Prints a README for this project and exits");
      return;
    }

    // Check if there's too many command line arguments
    if (argList.size() > 7) {
      System.err.println("Too many command line arguments");
      System.err.println("usage: java -jar target/phonebill-1.0.0.jar [options] customer callerNumber calleeNumber beginDate beginTime endDate endTime");
      return;
    }

    // Parse command line and get required arguments
    String name = argList.getFirst();
    String callerNum;
    String calleeNum;
    String beginDate;
    String beginTime;
    String endDate;
    String endTime;
    try {
      callerNum = argList.get(1);
      calleeNum = argList.get(2);
      beginDate = argList.get(3);
      beginTime = argList.get(4);
      endDate = argList.get(5);
      endTime = argList.get(6);
    } catch (IndexOutOfBoundsException e) {
      if (e.getMessage().contains("Index 1")) {
        System.err.println("Missing Caller Number");
      } else if (e.getMessage().contains("Index 2")) {
        System.err.println("Missing Callee Number");
      } else if (e.getMessage().contains("Index 3")) {
        System.err.println("Missing Begin Date");
      } else if (e.getMessage().contains("Index 4")) {
        System.err.println("Missing Begin Time");
      } else if (e.getMessage().contains("Index 5")) {
        System.err.println("Missing End Date");
      } else if (e.getMessage().contains("Index 6")) {
        System.err.println("Missing End Time");
      }
      return;
    }

    String begin = beginDate + " " + beginTime;
    String end = endDate + " " + endTime;

    if (!isValidPhoneNumber(callerNum)) {
      System.err.println("Invalid Caller Phone Format. Phone format should be ###-###-####.");
      return;
    } else if (!isValidPhoneNumber(calleeNum)) {
      System.err.println("Invalid Callee Phone Format. Phone format should be ###-###-####.");
      return;
    } else if (!isValidDateAndTime(begin)) {
      System.err.println("Invalid Begin Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm");
      return;
    } else if (!isValidDateAndTime(end)) {
      System.err.println("Invalid End Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm");
      return;
    }

    // Create a phone call attached to a phone bill after verification is successful
    PhoneCall newCall = new PhoneCall(callerNum, calleeNum, begin, end);
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