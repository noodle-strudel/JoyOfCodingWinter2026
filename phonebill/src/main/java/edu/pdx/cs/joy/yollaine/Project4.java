package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.jdbc.H2DatabaseHelper;
import org.jspecify.annotations.Nullable;

import java.io.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class for the Phone Bill Program in Project4.
 */
public class Project4 {

  /**
   * Maximum amount of arguments allowed on the command line.
   */
  private static final int MAX_ARGS = 9;

  /**
  * Determines if a date and time is valid.
  * @param dateAndTime
  *      The data and time combo to validate.
  * @return
  *      Returns true if dataAndTime is valid and false if it's invalid.
  */
  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    Pattern p = Pattern.compile("^(0?[1-9]|1[012])/((3[01])|([12][0-9])|0?[1-9])/[1-9][0-9]{3} (0?[0-9]|1[012]):[0-5][0-9] [AP]M$");
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

    // remove any empty arguments
    argList.removeIf(String::isEmpty);
    progFlags flags = getProgFlags(argList);
    // An exception or error occurred while getting flags for the program
    if (flags == null) return;

    // Print out the README and exit the program
    if (flags.readMeFlag()) {
      try (InputStream readme = Project4.class.getResourceAsStream("README.txt")
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
    if (flags.unknownArgFlag()) {
      System.err.println("Unknown option " + flags.unknownArg());
      System.err.println("options are (options may appear in any order before arguments):");
      System.err.println("\t-pretty file    Pretty print the phone bill to a text file");
      System.err.println("\t                or standard out (-pretty -)");
      System.err.println("\t-textFile file  Where to read/write the phone bill");
      System.err.println("\t-print          Prints a description of the new phone call");
      System.err.println("\t-README         Prints a README for this project and exits");
      return;
    }


    if (argList.size() > MAX_ARGS) {
      System.err.println("Too many command line arguments");
      System.err.println("usage: java -jar target/phonebill-1.0.0.jar [options] customer callerNumber calleeNumber beginDate beginTime AM/PM endDate endTime AM/PM");
      return;
    }


    // Parse command line and get required arguments
    String name = argList.getFirst();
    String callerNum;
    String calleeNum;
    String beginDate = "";
    String beginTime;
    String beginAMPM;
    String endDate;
    String endTime;
    String endAMPM;
    try {
      callerNum = argList.get(1);
      calleeNum = argList.get(2);
      beginDate = argList.get(3);
      beginTime = argList.get(4);
      beginAMPM = argList.get(5);
      endDate = argList.get(6);
      endTime = argList.get(7);
      endAMPM = argList.get(8);
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
        System.err.println("Missing Begin Time AM/PM");
      } else if (e.getMessage().contains("Index 6")) {
        System.err.println("Missing End Date");
      } else if (e.getMessage().contains("Index 7")) {
        System.err.println("Missing End Time");
      } else {
        System.err.println("Missing End Time AM/PM");
      }
      return;
    }

    String begin = beginDate + " " + beginTime + " " + beginAMPM;
    String end = endDate + " " + endTime + " " + endAMPM;

    if (!isValidPhoneNumber(callerNum)) {
      System.err.println("Invalid Caller Phone Format. Phone format should be ###-###-####.");
      return;
    } else if (!isValidPhoneNumber(calleeNum)) {
      System.err.println("Invalid Callee Phone Format. Phone format should be ###-###-####.");
      return;
    }

    if (flags.textFileFlag() && flags.prettyFileFlag() && flags.textFileArg().equals(flags.prettyFileArg())) {
      System.err.println("Error: -pretty and -textFile share the same file location.");
      return;
    } else if (flags.dbFileFlag() && flags.prettyFileFlag() && flags.dbFileArg().equals(flags.prettyFileArg())) {
      System.err.println("Error: -pretty and -dbFile share the same file location.");
      return;
    }


    // Check if the phone bill will be read to a text file
    boolean newFile = false;
    PhoneBill newBill = null;
    File phoneBillFile = null;

    if (flags.textFileFlag()) {
      phoneBillFile = new File(flags.textFileArg());

      // check if given file is directory; error and exit if so
      if (phoneBillFile.isDirectory()) {
        System.err.println("Unable to process file: Given file is a directory.");
        return;
      }

      // otherwise, either create a new file or extract a PhoneBill from it.
      try {
        newFile = phoneBillFile.createNewFile();
        if (!newFile) {
          TextParser parser = new TextParser(new FileReader(phoneBillFile));
          newBill = parser.parse();
        }
      } catch (IOException | ParserException e) {
        System.err.println(e.getMessage());
        return;
      }
    } else if (flags.dbFileFlag()) {
      File dbFile = new File(flags.dbFileArg());
      if (!dbFile.exists()) {
        System.err.println("Error: "+dbFile.getAbsolutePath()+" does not exist.");
        return;
      }
      try (Connection connection = H2DatabaseHelper.createFileBasedConnection(dbFile)) {
        PhoneBillDAO.createTables(connection);
        DatabaseLoader dbd = new DatabaseLoader(connection);
        newBill = dbd.parseNamed(name);
      } catch (ParserException | SQLException e) {
        System.err.println("An error occurred while parsing the database: "+e.getMessage());
      }
    }

    // Create a phone call attached to a phone bill after verification is successful
    PhoneCall newCall;
    try {
      newCall = new PhoneCall(callerNum, calleeNum, begin, end);
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      return;
    }

    // create a new phonebill if it wasn't from a file or a textFile option is done but there's no preexisting file
    if (!flags.dbFileFlag() && (!flags.textFileFlag() || newFile)) {
      newBill = new PhoneBill(name);

    } else { // otherwise, there is a valid phone bill with at least a customer name
      if (!Objects.equals(newBill.getCustomer(), name)) {

        System.err.println("Unmatching customer names in provided file: Expected " + name + " but found " + newBill.getCustomer() + ".");
        return;
      }

      // ensure the new phone call is not a duplicate
      boolean detectedDup = false;
      for (PhoneCall call : newBill.getPhoneCalls()) {
        if (call.equals(newCall)) {
          detectedDup = true;
          break;
        }
      }

      if (detectedDup) {
        System.err.println("Unable to add new phone call to file: Exact phone call already recorded.");
        return;
      }

    }

    newBill.addPhoneCall(newCall);

    if (flags.prettyFileFlag()) {
      Writer writer;
      if (flags.prettyFileArg().equals("-")) {
        writer = new StringWriter();
      } else {
        File phoneBillPrettyFile = new File(flags.prettyFileArg());
        if (phoneBillPrettyFile.isDirectory()) {
          System.err.println("Unable to process file: Given file is a directory.");
          return;
        }

        try {
          writer = new FileWriter(phoneBillPrettyFile);

        } catch (IOException e) {
          System.err.println(e.getMessage());
          return;
        }
      }

      // if the file is all good, print out the bill to it
      PrettyPrinter prettyPrinter = new PrettyPrinter(writer);
      prettyPrinter.dump(newBill);

      if (writer instanceof StringWriter) {
        System.out.println(writer);
      }
    }

    if (flags.textFileFlag()) {
      try {
        TextDumper textDumper = new TextDumper(new FileWriter(phoneBillFile));
        textDumper.dump(newBill);
      } catch (IOException e) {
        System.err.println(e.getMessage());
        return;
      }
    } else if (flags.dbFileFlag()) {
      File dbFile = new File(flags.dbFileArg());
      try (Connection connection = H2DatabaseHelper.createFileBasedConnection(dbFile)) {
        PhoneBillDAO.createTables(connection);
        DatabaseDumper dbd = new DatabaseDumper(connection);
        dbd.dump(newBill);
        PhoneBillDAO.writeOut(connection, dbFile);
      } catch (SQLException | RuntimeException e) {
        System.err.println("An error occurred while creating the database: " + e.getMessage());
        return;
      }
    }

    if (flags.printFlag()) {
      System.out.println(newBill);
      System.out.println("Newest " + newCall);
    }
  }

  /**
   * Helper function to process options on the command line.
   * @param argList
   *    The list of arguments given on the command line.
   * @return
   *    Returns a record of program flags and an unknown argument if flagged.
   */
  private static @Nullable progFlags getProgFlags(ArrayList<String> argList) {
    // Process options until a non-option is encountered
    boolean printFlag = false;
    boolean readMeFlag = false;
    boolean unknownArgFlag = false;
    boolean textFileFlag = false;
    boolean prettyFileFlag = false;
    boolean dbFileFlag = false;
    String textFileArg = "";
    String prettyFileArg = "";
    String unknownArg = "";
    String currentArg;
    String dbFileArg = "";
    try {
      while ((currentArg = argList.getFirst()).startsWith("-")) {
        if (currentArg.contains("textFile")) {
          if (dbFileFlag) {
            throw new IllegalArgumentException("Incompatible flags -textFile and -dbFile: Only one can be specified at a time");
            }
          argList.removeFirst();
          textFileFlag = true;
          textFileArg = argList.removeFirst();
        } else if (currentArg.contains("pretty")) {
          argList.removeFirst();
          prettyFileFlag = true;
          prettyFileArg = argList.removeFirst();
        } else if (currentArg.contains("dbFile")) {
          if (textFileFlag) {
            throw new IllegalArgumentException("Incompatible flags -textFile and -dbFile: Only one can be specified at a time");
          }
          argList.removeFirst();
          dbFileFlag = true;
          dbFileArg = argList.removeFirst();
        } else {
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
      }
    } catch (NoSuchElementException e) {
      // An exception was thrown because there was no argument for the textFile or pretty option.
      if (textFileFlag && textFileArg.isEmpty()) {
        System.err.println("-textFile argument requires file path");
        return null;
      } else if (prettyFileFlag && prettyFileArg.isEmpty()) {
        System.err.println("-pretty argument requires file path or \"-\" for standard output.");
        return null;
      } else if (dbFileFlag && dbFileArg.isEmpty()) {
        System.err.println("-dbFile argument requires file path");
        return null;
        // If there's no -README flag and no unknown arguments, then the program will try to
        // store phone bill information without exiting.
        // But since there's no arguments and only options, it will give an error message.
      } else if (!readMeFlag && !unknownArgFlag) {
        System.err.println("Missing phone bill information");
        System.err.println("usage: java -jar target/phonebill-1.0.0.jar [options] customer callerNumber calleeNumber beginDate beginTime AM/PM endDate endTime AM/PM");
        return null;
      }
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      return null;
    }
    return new progFlags(printFlag, readMeFlag, unknownArgFlag, textFileFlag, prettyFileFlag, dbFileFlag, unknownArg, textFileArg, prettyFileArg, dbFileArg);
  }

  /**
   * A record of all the flags for the Project4 program.
   *
   * @param printFlag
   *    Prints the {@link PhoneBill} to standard out.
   * @param readMeFlag
   *    Prints the README and exits the program.
   * @param unknownArgFlag
   *    Prints the unknown argument error to standard error and exits the program.
   * @param textFileFlag
   *    Reads and possibly creates a given text file to parse and write a PhoneBill to.
   * @param prettyFileFlag
   *    Prints the PhoneBill in a "pretty" format meant for viewing.
   * @param dbFileFlag
   *    Reads and adds a PhoneBill to a relational database file.
   * @param unknownArg
   *    The unknown argument detected.
   * @param textFileArg
   *    The argument for the text file to perform I/O on.
   * @param prettyFileArg
   *    The argument for the pretty file to perform output on.
   * @param dbFileArg
   *    The argument for the database file to add the phone bill to.
   */
  private record progFlags(boolean printFlag, boolean readMeFlag, boolean unknownArgFlag, boolean textFileFlag, boolean prettyFileFlag, boolean dbFileFlag, String unknownArg,
                           String textFileArg, String prettyFileArg, String dbFileArg) {
    /**
     * The constructor for the progFlag record.
     *
     * @param printFlag
     *    Prints the {@link PhoneBill} to standard out.
     * @param readMeFlag
     *    Prints the README and exits the program.
     * @param unknownArgFlag
     *    Prints the unknown argument error to standard error and exits the program.
     * @param textFileFlag
     *    Reads and possibly creates a given text file to parse and write a PhoneBill to.
     * @param prettyFileFlag
     *    Prints the PhoneBill in a "pretty" format meant for viewing.
     * @param dbFileFlag
     *    Reads and adds a PhoneBill to a relational database file.
     * @param unknownArg
     *    The unknown argument detected.
     * @param textFileArg
     *    The argument for the text file to perform I/O on.
     * @param prettyFileArg
     *    The argument for the pretty file to perform output on.
     * @param dbFileArg
     *    The argument for the database file to add the phone bill to.
     */
    private progFlags {
    }
  }
}
