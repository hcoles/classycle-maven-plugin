package org.pitest.classycle;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Run classycle analysis
 * 
 * @goal analyse
 * @requiresProject true
 */
public class ClassycleAnalyseMojo extends AbstractMojo implements Project {

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
   * Calculate only package dependencies.
   * 
   * @parameter default-value="false"
   */
  private boolean      packagesOnly;

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
  
  /**
   * Title to use for report
   * 
   * @parameter expression="${project.artifactId}"
   */
  private String title;

  public final void execute() throws MojoExecutionException,
      MojoFailureException {

    try {
      final ClassycleAnalyser analyser = new ClassycleAnalyser(this,
          new StreamFactory(this.getTargetDirectory() + File.separator + "classycle"));
      analyser.analyse();

    } catch (final IOException e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

  public boolean isMergeInnerClasses() {
    return this.mergeInnerClasses;
  }

  public String getOutputDirectory() {
    return this.project.getBuild().getOutputDirectory();
  }

  public boolean isPackagesOnly() {
    return this.packagesOnly;
  }

  public String getTargetDirectory() {
    return this.project.getBuild().getDirectory();
  }

  public List<String> getReflectionPatterns() {

    return this.reflectionPatterns;
  }

  public List<String> getIncludingClasses() {
    return this.includingClasses;
  }

  public List<String> getExcludingClasses() {
    return this.excludingClasses;
  }

  public String getTitle() {
    return this.title;
  }

}