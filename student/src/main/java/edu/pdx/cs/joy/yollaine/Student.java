package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.lang.Human;

import java.util.ArrayList;

/**
 * This class represents a <code>Student</code>.
 */
public class Student extends Human {

  private double gpa;

  /**
   * Creates a new <code>Student</code>
   *
   * @param name
   *        The student's name
   * @param classes
   *        The names of the classes the student is taking.  A student
   *        may take zero or more classes.
   * @param gpa
   *        The student's grade point average
   * @param gender
   *        The student's gender ("male", "female", or "other", case-insensitive)
   */
  public Student(String name, ArrayList<String> classes, double gpa, String gender) {
    super(name);
    this.gpa = gpa;
  }

  /**
   * All students say "This class is too much work"
   */
  @Override
  public String says() {
    return "This class is too much work";
  }

  /**
   * Returns a <code>String</code> that describes this
   * <code>Student</code>.
   */
  public String toString() {
    return this.getName() + " has a GPA of " + gpa;
  }

  /**
   * Main program that parses the command line, creates a
   * <code>Student</code>, and prints a description of the student to
   * standard out by invoking its <code>toString</code> method.
   */
  public static void main(String[] args) {
    System.err.println("Missing required student information!!");
  }
}