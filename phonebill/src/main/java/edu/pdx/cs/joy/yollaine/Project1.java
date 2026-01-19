package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;

/**
 * The main class for the Phone Bill Project
 */
public class Project1 {

  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    return true;
  }

  public static void main(String[] args) {
    PhoneCall call = new PhoneCall();  // Refer to one of Dave's classes so that we can be sure it is on the classpath
    if (args.length == 0) {
      System.err.println("Missing phone bill information");
      return;
    }

    String name = args[0];


    for (String arg : args) {
      System.out.println(arg);
    }
  }

}