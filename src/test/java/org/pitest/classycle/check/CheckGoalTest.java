package org.pitest.classycle.check;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.CharEncoding;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pitest.classycle.StreamFactory;

public class CheckGoalTest {

  private final String                             CHECK_THAT_WILL_FAIL     = "[A] = Doesnotexit \n check sets [A]";
  private final String                             CHECK_THAT_WILL_NOT_FAIL = "[A] = * \n check sets [A]";

  @Mock
  private CheckProject                             project;

  private final Map<String, ByteArrayOutputStream> output                   = new HashMap<String, ByteArrayOutputStream>();

  private CheckGoal                                testee;

  private StreamFactory                            sf;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(this.project.getOutputDirectory()).thenReturn(testOutput());
    when(this.project.getExcludingClasses()).thenReturn(null);
    when(this.project.getReportEncoding()).thenReturn(CharEncoding.UTF_8);
    this.sf = createStreamFactory();
    this.testee = new CheckGoal(this.project, this.sf);
  }

  private StreamFactory createStreamFactory() {
    return new StreamFactory(null) {
      @Override
      public OutputStream createStream(final String fileName)
          throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        CheckGoalTest.this.output.put(fileName, os);
        return os;
      }
    };
  }

  private String testOutput() {
    final File f = new File("target" + File.separator + "test-classes");
    return f.getAbsolutePath();
  }

  @Test
  public void shouldFailBuildWhenCheckViolatedAndFlagSet() throws IOException {
    when(this.project.isFailOnUnWantedDependencies()).thenReturn(true);
    when(this.project.getDependencyDefinition()).thenReturn(
        this.CHECK_THAT_WILL_FAIL);
    assertFalse(this.testee.analyse());
  }

  @Test
  public void shouldNotFailBuildWhenCheckViolatedAndFlagNotSet()
      throws IOException {
    when(this.project.isFailOnUnWantedDependencies()).thenReturn(false);
    when(this.project.getDependencyDefinition()).thenReturn(
        this.CHECK_THAT_WILL_FAIL);
    assertTrue(this.testee.analyse());
  }

  @Test
  public void shouldNotFailBuildWhenNoChecksViolatedAndFlagSet()
      throws IOException {
    when(this.project.isFailOnUnWantedDependencies()).thenReturn(true);
    when(this.project.getDependencyDefinition()).thenReturn(
        this.CHECK_THAT_WILL_NOT_FAIL);
    assertTrue(this.testee.analyse());
  }

  @Test
  public void shouldNotFailBuildWhenNotCheckViolatedAndFlagNotSet()
      throws IOException {
    when(this.project.isFailOnUnWantedDependencies()).thenReturn(false);
    when(this.project.getDependencyDefinition()).thenReturn(
        this.CHECK_THAT_WILL_NOT_FAIL);
    assertTrue(this.testee.analyse());
  }

  @Test
  public void shouldWriteReportToFile() throws IOException {
    when(this.project.getDependencyDefinition()).thenReturn(
        this.CHECK_THAT_WILL_NOT_FAIL);
    this.testee.analyse();
    System.out.println(this.output.get(CheckGoal.RESULTS_FILE));
    assertThat(this.output.get(CheckGoal.RESULTS_FILE)).isNotNull();
  }

}
