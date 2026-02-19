package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.jdbc.H2DatabaseHelper;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Converts a {@link PhoneBill} text file created by {@link TextDumper} to a database file.
 */
public class Converter {

  /**
   * Converts a PhoneBill to a database file.
   * @param bill PhoneBill to convert.
   * @param dbFile Target database file to create.
   * @throws SQLException if an error occurs while accessing the database.
   */
  public static void phoneBillToDatabase(PhoneBill bill, File dbFile) throws SQLException {
    try (Connection connection = H2DatabaseHelper.createFileBasedConnection(dbFile)) {
      PhoneBillDAO.createTables(connection);
      PhoneBillDAO billDAO = new PhoneBillDAO(connection);

      billDAO.save(bill);
      PhoneBillDAO.writeOut(connection, dbFile);
    }
  }

  /**
   * The main method of the Converter.
   * @param args Arguments passed in from the command line.
   */
  public static void main(String @NonNull [] args) {
    if (args.length == 0) {
      System.err.println("Missing command line arguments");
      System.err.println("usage: java -cp target/phonebill-1.0.0.jar edu.pdx.cs.joy.<login-id>.Converter textFile dbFile");
      System.err.println("\ttextFile\tThe name of the text file to conver");
      System.err.println("\tdbFile\t\tThe name of the file containing the database");
      return;
    } else if (args.length == 1) {
      System.err.println("Missing database file");
      System.err.println("usage: java -cp target/phonebill-1.0.0.jar edu.pdx.cs.joy.<login-id>.Converter textFile dbFile");
      System.err.println("\ttextFile\tThe name of the text file to conver");
      System.err.println("\tdbFile\t\tThe name of the file containing the database");
      return;
    } else if (args.length > 2) {
      System.err.println("Too many command line arguments");
      System.err.println("usage: java -cp target/phonebill-1.0.0.jar edu.pdx.cs.joy.<login-id>.Converter textFile dbFile");
      System.err.println("\ttextFile\tThe name of the text file to conver");
      System.err.println("\tdbFile\t\tThe name of the file containing the database");
    }
    File textFile = new File(args[0]);
    File dbFile = new File(args[1]);
    try {
      TextParser tp = new TextParser(new FileReader(textFile));
      PhoneBill bill = tp.parse();

      try (Connection connection = H2DatabaseHelper.createFileBasedConnection(dbFile)) {
        PhoneBillDAO.createTables(connection);
        DatabaseDumper dbd = new DatabaseDumper(connection);
        dbd.dump(bill);
        PhoneBillDAO.writeOut(connection, dbFile);
      }
    } catch (FileNotFoundException e) {
      if (dbFile.isDirectory() || textFile.isDirectory()) {
        System.err.println("Error while reading files: At least 1 File is a directory");
      } else {
        System.err.println("Error while reading files: Phone bill text file does not exist");
      }
    } catch (ParserException e) {
      System.err.println("Error while parsing text file: "+e.getMessage());
    } catch (SQLException e) {
      System.err.println("Error while accessing database: "+e.getMessage());
    }
  }
}
