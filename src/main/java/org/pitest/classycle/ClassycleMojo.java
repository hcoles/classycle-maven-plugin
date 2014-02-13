package org.pitest.classycle;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

public abstract class ClassycleMojo extends AbstractMojo implements Project {

  /**
   * <i>Internal</i>: Project to interact with.
   * 
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * Merge inner class vertices with their outer classes.
   * 
   * @parameter default-value="true"
   */
  private boolean      mergeInnerClasses;

  /**
   * @parameter expression="${project.reporting.outputEncoding}" default-value="UTF-8"
   */
  protected String reportEncoding;

  /**
   * List of zero or more wild-card patterns for fully-qualified class names
   * which are referred in the class file by plain string constants. Only '*' is
   * interpreted as wild-card character.
   * 
   * @parameter
   */
  private List<String> reflectionPatterns;

  /**
   * List of zero or more classes to include.
   * 
   * @parameter
   */
  private List<String> includingClasses;

  /**
   * List of zero or more classes to exclude.
   * 
   * @parameter
   */
  private List<String> excludingClasses;

  public final boolean isMergeInnerClasses() {
    return this.mergeInnerClasses;
  }

  public final String getOutputDirectory() {
    return this.project.getBuild().getOutputDirectory();
  }

  public final String getTargetDirectory() {
    return this.project.getBuild().getDirectory();
  }

  public final List<String> getReflectionPatterns() {

    return this.reflectionPatterns;
  }

  public final List<String> getIncludingClasses() {
    return this.includingClasses;
  }

  public final List<String> getExcludingClasses() {
    return this.excludingClasses;
  }

  public final String getReportEncoding() {
    return this.reportEncoding;
  }
}