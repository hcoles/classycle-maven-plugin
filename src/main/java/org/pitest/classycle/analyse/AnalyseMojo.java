package org.pitest.classycle.analyse;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.pitest.classycle.ClassycleMojo;
import org.pitest.classycle.StreamFactory;

/**
 * Run classycle analysis
 * 
 * @goal analyse
 * @requiresProject true
 */
public class AnalyseMojo extends ClassycleMojo implements AnalyseProject {

  /**
   * Calculate only package dependencies.
   * 
   * @parameter default-value="false"
   */
  private boolean packagesOnly;

  /**
   * Title to use for report
   * 
   * @parameter expression="${project.artifactId}"
   */
  private String  title;

  public final void execute() throws MojoExecutionException,
      MojoFailureException {

    try {
      final AnalyseGoal analyser = new AnalyseGoal(this, new StreamFactory(
          this.getTargetDirectory() + File.separator + "classycle"));
      analyser.analyse();

    } catch (final IOException e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

  public boolean isPackagesOnly() {
    return this.packagesOnly;
  }

  public String getTitle() {
    return this.title;
  }

}