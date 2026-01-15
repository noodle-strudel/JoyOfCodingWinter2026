package edu.pdx.cs.joy.yollaine;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Unit tests for the Student class.  In addition to the JUnit annotations,
 * they also make use of the <a href="http://hamcrest.org/JavaHamcrest/">hamcrest</a>
 * matchers for more readable assertion statements.
 */
public class StudentTest
{

  @Test
  void studentNamedPatIsNamedPat() {
    String name = "Pat";
    var pat = createStudentNamed(name);
    assertThat(pat.getName(), equalTo(name));
  }

  private static @NonNull Student createStudentNamed(String name) {
    return new Student(name, new ArrayList<>(), 0.0, "Doesn't matter");
  }

  @Test
  void allStudentsSayThisClassIsTooMuchWork() {
    var student = createStudentNamed("Any Name");
    assertThat(student.says(), equalTo("This class is too much work"));
  }

  @Disabled
  @Test
  void daveStudent() {
    ArrayList<String> classes = new ArrayList<>();
    classes.add("Algorithms");
    classes.add("Operating Systems");
    classes.add("Java");
    Student dave = new Student("Dave", classes, 3.64, "male");

    assertThat(dave.toString(), equalTo("Dave has a GPA of 3.64 and is taking 3 classes: Algorithms, Operating Systems, and Java. He says: This class is too much work"));
  }
  
  @Test
  void toStringContainsStudentName() {
    String name = "Student";
    Student student = createStudentNamed(name);
    assertThat(student.toString(), containsString(name));
  }

  @Test
  void toStringContainsGPA() {
    double gpa = 3.5;
    Student student = new Student("Any name", new ArrayList<>(), gpa, "Doesn't Matter");
    assertThat(student.toString(), containsString(" has a GPA of " + gpa));
  }

}
