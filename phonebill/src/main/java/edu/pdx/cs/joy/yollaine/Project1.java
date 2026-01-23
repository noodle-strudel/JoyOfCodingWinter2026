package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Arrays;

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

        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));

        boolean printFlag = argList.contains("-print");
        if (printFlag) {
            argList.removeIf(arg -> arg.equals("-print"));
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


        PhoneCall newCall = new PhoneCall(callerNum, calleeNum, beginTime, endTime);
        PhoneBill newBill = new PhoneBill(name);

        newBill.addPhoneCall(newCall);

        if (printFlag) {
            System.out.println(newBill);
        }
    /*
    for (String arg : args) {
    System.out.println(arg);
    }

    */
    }

}