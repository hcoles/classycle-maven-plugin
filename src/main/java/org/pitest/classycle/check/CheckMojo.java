package org.pitest.classycle.check;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.pitest.classycle.ClassycleMojo;
import org.pitest.classycle.StreamFactory;

import classycle.util.Text;

/**
 * Checks a project against a set of Classycle rules
 * 
 * @goal check
 * @requiresProject true
 */
public class CheckMojo extends ClassycleMojo implements CheckProject {

  /**
   * Script to check code against
   * 
   * @parameter
   */
  private String  dependencyDefinitionFile;
  
  /**
   * Embedded dependency check definition
   * 
   * @parameter
   */
  private String dependencyDefinition;

  /**
   * Script to check code against
   * 
   * @parameter default-value="true"
   * @required
   */
  private boolean failOnUnWantedDependencies;

  public final void execute() throws MojoExecutionException,
      MojoFailureException {

    try {
      final CheckGoal checker = new CheckGoal(this, new StreamFactory(
          this.getTargetDirectory() + File.separator + "classycle"));
      if (!checker.analyse()) {
        throw new MojoExecutionException(
            "Unwanted dependencies found. See output for details.");
      }

    } catch (final IOException e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

  public String getDependencyDefinition() throws IOException {
    if ( dependencyDefinitionFile != null ) {
      return Text.readTextFile(new File(this.dependencyDefinitionFile));
    } else {
      return dependencyDefinition;
    }
  }

  public boolean isFailOnUnWantedDependencies() {
    return this.failOnUnWantedDependencies;
  }

}