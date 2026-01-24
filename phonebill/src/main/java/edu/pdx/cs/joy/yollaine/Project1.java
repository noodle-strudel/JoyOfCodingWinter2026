package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;

import java.io.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * The main class for the Phone Bill Project
 */
public class Project1 {

    @VisibleForTesting
    static boolean isValidDateAndTime(String dateAndTime) {
        return !dateAndTime.isEmpty();
    }

    public static void main(String[] args) {
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));

        boolean printFlag = false;
        boolean readMeFlag = false;
        boolean unknownFlag = false;
        String currentArg = "";
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
                StringBuffer readMeOutput = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
                String line = "";

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
            // TODO: add usage stuff
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