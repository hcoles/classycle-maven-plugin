package org.pitest.classycle;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ClassycleAnalyserTest {

  @Mock
  private Project           project;

  private StringWriter      writer;

  private ClassycleAnalyser testee;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(this.project.getOutputDirectory()).thenReturn(testOutput());
    when(this.project.getExcludingClasses()).thenReturn(null);
    this.writer = new StringWriter();

    this.testee = new ClassycleAnalyser(this.project) {
      @Override
      Writer createWriter(final File file) throws IOException {
        return ClassycleAnalyserTest.this.writer;
      }

    };
  }

  private String testOutput() {
    final File f = new File("target" + File.separator + "test-classes");
    return f.getAbsolutePath();
  }

  @Test
  public void shouldNotMergeInnerClassesWhenFlagSetToFalse() throws IOException {
    when(this.project.isMergeInnerClasses()).thenReturn(false);
    this.testee.analyse();
    assertThat(this.writer.toString()).contains(
        classEntryFor("com.example.Bar$NestedBar"));
  }

  @Test
  public void shouldMergeInnerClassesWhenFlagSetToTrue() throws IOException {
    when(this.project.isMergeInnerClasses()).thenReturn(true);
    this.testee.analyse();
    assertThat(this.writer.toString()).doesNotContain(
        classEntryFor("com.example.Bar$NestedBar"));
  }

  @Test
  public void shouldCalculateClassDependenciesWhenPackageOnlyFlagNotSet()
      throws IOException {
    when(this.project.isPackagesOnly()).thenReturn(false);
    this.testee.analyse();
    assertThat(this.writer.toString()).contains("<classes");
  }

  @Test
  public void shouldNotCalculateClassDependenciesWhenPackageOnlyFlagSet()
      throws IOException {
    when(this.project.isPackagesOnly()).thenReturn(true);
    this.testee.analyse();
    assertThat(this.writer.toString()).doesNotContain("<classes");
  }

  @Test
  public void shouldIncludeAllClassesWhenNoneExcluded() throws IOException {
    when(this.project.getExcludingClasses()).thenReturn(null);
    this.testee.analyse();
    assertThat(this.writer.toString()).contains(
        classEntryFor("com.example.Foo"));
  }

  @Test
  public void shouldExcludeSingleClassFromAnalysisWhenOneSupplied()
      throws IOException {
    when(this.project.getExcludingClasses()).thenReturn(
        Arrays.asList("com.example.Foo"));
    this.testee.analyse();
    assertThat(this.writer.toString()).doesNotContain(
        classEntryFor("com.example.Foo"));
  }

  @Test
  public void shouldExcludeClassesFromAnalysisWhenMoreThanOneSupplied()
      throws IOException {
    when(this.project.getExcludingClasses()).thenReturn(
        Arrays.asList("com.example.Foo", "com.example.Bar"));
    this.testee.analyse();
    assertThat(this.writer.toString()).doesNotContain(
        classEntryFor("com.example.Foo"));
    assertThat(this.writer.toString()).doesNotContain(
        classEntryFor("com.example.Bar"));
  }

  @Test
  public void shouldOnlyIncludeClassesMatchingFilerWhenOneSupplied()
      throws IOException {
    when(this.project.getIncludingClasses()).thenReturn(
        Arrays.asList("com.example.Foo"));
    this.testee.analyse();
    assertThat(this.writer.toString()).contains(
        classEntryFor("com.example.Foo"));
    assertThat(this.writer.toString()).doesNotContain(
        classEntryFor("com.example.Bar"));
  }

  @Test
  public void shouldOnlyIncludeClassesMatchingFilerWhenMoreThanOneSupplied()
      throws IOException {
    when(this.project.getIncludingClasses()).thenReturn(
        Arrays.asList("com.example.Foo", "com.example.Bar"));
    this.testee.analyse();
    assertThat(this.writer.toString()).contains(
        classEntryFor("com.example.Foo"));
    assertThat(this.writer.toString()).contains(
        classEntryFor("com.example.Bar"));
  }

  private String classEntryFor(final String clazz) {
    return "<class name=\"" + clazz + "\"";
  }

}
