package org.pitest.classycle;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ClassycleAnalyserTest {

  @Mock
  private Project                                  project;

  private final Map<String, ByteArrayOutputStream> output = new HashMap<String, ByteArrayOutputStream>();

  private ClassycleAnalyser                        testee;

  private StreamFactory                            sf;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(this.project.getOutputDirectory()).thenReturn(testOutput());
    when(this.project.getExcludingClasses()).thenReturn(null);
    this.sf = createStreamFactory();
    this.testee = new ClassycleAnalyser(this.project, this.sf);
  }

  private StreamFactory createStreamFactory() {
    return new StreamFactory(null) {
      @Override
      public OutputStream createStream(final String fileName)
          throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        ClassycleAnalyserTest.this.output.put(fileName, os);
        return os;
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
    assertThat(xmlOutput())
        .contains(classEntryFor("com.example.Bar$NestedBar"));
  }

  @Test
  public void shouldMergeInnerClassesWhenFlagSetToTrue() throws IOException {
    when(this.project.isMergeInnerClasses()).thenReturn(true);
    this.testee.analyse();
    assertThat(xmlOutput()).doesNotContain(
        classEntryFor("com.example.Bar$NestedBar"));
  }

  @Test
  public void shouldCalculateClassDependenciesWhenPackageOnlyFlagNotSet()
      throws IOException {
    when(this.project.isPackagesOnly()).thenReturn(false);
    this.testee.analyse();
    assertThat(xmlOutput()).contains("<classes");
  }

  @Test
  public void shouldNotCalculateClassDependenciesWhenPackageOnlyFlagSet()
      throws IOException {
    when(this.project.isPackagesOnly()).thenReturn(true);
    this.testee.analyse();
    assertThat(xmlOutput()).doesNotContain("<classes");
  }

  @Test
  public void shouldIncludeAllClassesWhenNoneExcluded() throws IOException {
    when(this.project.getExcludingClasses()).thenReturn(null);
    this.testee.analyse();
    assertThat(xmlOutput()).contains(classEntryFor("com.example.Foo"));
  }

  @Test
  public void shouldExcludeSingleClassFromAnalysisWhenOneSupplied()
      throws IOException {
    when(this.project.getExcludingClasses()).thenReturn(
        Arrays.asList("com.example.Foo"));
    this.testee.analyse();
    assertThat(xmlOutput()).doesNotContain(classEntryFor("com.example.Foo"));
  }

  @Test
  public void shouldExcludeClassesFromAnalysisWhenMoreThanOneSupplied()
      throws IOException {
    when(this.project.getExcludingClasses()).thenReturn(
        Arrays.asList("com.example.Foo", "com.example.Bar"));
    this.testee.analyse();
    assertThat(xmlOutput()).doesNotContain(classEntryFor("com.example.Foo"));
    assertThat(xmlOutput()).doesNotContain(classEntryFor("com.example.Bar"));
  }

  @Test
  public void shouldOnlyIncludeClassesMatchingFilerWhenOneSupplied()
      throws IOException {
    when(this.project.getIncludingClasses()).thenReturn(
        Arrays.asList("com.example.Foo"));
    this.testee.analyse();
    assertThat(xmlOutput()).contains(classEntryFor("com.example.Foo"));
    assertThat(xmlOutput()).doesNotContain(classEntryFor("com.example.Bar"));
  }

  @Test
  public void shouldOnlyIncludeClassesMatchingFilerWhenMoreThanOneSupplied()
      throws IOException {
    when(this.project.getIncludingClasses()).thenReturn(
        Arrays.asList("com.example.Foo", "com.example.Bar"));
    this.testee.analyse();
    assertThat(xmlOutput()).contains(classEntryFor("com.example.Foo"));
    assertThat(xmlOutput()).contains(classEntryFor("com.example.Bar"));
  }

  @Test
  public void shouldWriteXSLTFileToOutputDir() throws IOException {
    this.testee.analyse();
    assertThat(output.get("reportXMLtoHTML.xsl")).isNotNull();
  }
  
  @Test
  public void shouldWriteImagesOutputDir() throws IOException {
    this.testee.analyse();
    assertThat(output.get("images/class.png")).isNotNull();
  }
  
  String xmlOutput() {
    final ByteArrayOutputStream bos = this.output
        .get(ClassycleAnalyser.XML_FILE);
    return new String(bos.toByteArray());
  }

  private String classEntryFor(final String clazz) {
    return "<class name=\"" + clazz + "\"";
  }

}
