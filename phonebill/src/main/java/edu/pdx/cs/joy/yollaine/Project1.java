package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;

/**
 * The main class for the Phone Bill Project
 */
public class Project1 {

  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    return !dateAndTime.isEmpty();
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Missing phone bill information");
      return;
    }

    String name = args[0];

    try {
      String callerNum = args[1];
      String calleeNum = args[2];
      String beginTime = args[3];
      String endTime = args[4];
    } catch (ArrayIndexOutOfBoundsException e) {
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


    for (String arg : args) {
      System.out.println(arg);
    }
  }

}