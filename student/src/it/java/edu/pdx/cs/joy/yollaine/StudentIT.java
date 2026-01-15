package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.InvokeMainTestCase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Integration tests for the <code>Student</code> class's main method.
 * These tests extend <code>InvokeMainTestCase</code> which allows them
 * to easily invoke the <code>main</code> method of <code>Student</code>.
 */
class StudentIT extends InvokeMainTestCase {

  @Test
  void invokingMainWithNoArgumentsPrintsMissingArgumentsToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Student.class);
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing required student information"));
  }


  @Disabled
  @Test
  void daveStudentIsWrittenToStandardOut() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Student.class, "Dave", "male", "3.64", "Algorithms", "Operating Systems", "Java");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Dave has a GPA of 3.64 and is taking 3 classes: Algorithms, Operating Systems, and Java. He says: This class is too much work"));
  }

  @Test
  void studentGPAIsWrittenToStandardOut() {
    String gpa = "3.5";
    InvokeMainTestCase.MainMethodResult result = invokeMain(Student.class, "Any Name", gpa);
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString(gpa));
  }

  @Test
  void invalidGPAIssuesErrorToStandardError() {
    String invalidGPA = "kkfkldkf";
    InvokeMainTestCase.MainMethodResult result =
            invokeMain(Student.class, "Any name", invalidGPA);
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid GPA: \"" + invalidGPA + "\""));
  }

}
