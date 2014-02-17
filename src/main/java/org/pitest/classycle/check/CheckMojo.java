package org.pitest.classycle.check;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.pitest.classycle.ClassycleMojo;
import org.pitest.classycle.StreamFactory;

import classycle.dependency.ResultRenderer;
import classycle.dependency.XMLResultRenderer;
import classycle.util.Text;

/**
 * Checks a project against a set of Classycle rules
 * 
 * @goal check
 * @requiresProject true
 */
public class CheckMojo extends ClassycleMojo implements CheckProject {
  public static final String TEXT_RESULT_FILE = "checkresults.txt";
  public static final String XML_RESULT_FILE = "checkresults.xml";

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

  /**
   * @parameter default-value="classycle.dependency.DefaultResultRenderer"
   */
  private String resultRenderer;

  /**
   * @parameter
   */
  private String outputFile;

  private Class<? extends ResultRenderer> resultRendererClass;

  public final void execute() throws MojoExecutionException,
      MojoFailureException {

    try {
      Class<?> clazz = this.getClass().getClassLoader().loadClass(resultRenderer);

      if(!ResultRenderer.class.isAssignableFrom(clazz)) {
        throw new MojoExecutionException("resultRenderer should implement ResultRenderer. " + resultRenderer + " does not!");
      }

      resultRendererClass = (Class<? extends ResultRenderer>)this.getClass().getClassLoader().loadClass(resultRenderer);

      final CheckGoal checker = new CheckGoal(this, new StreamFactory(
          this.getTargetDirectory() + File.separator + "classycle"));
      if (!checker.analyse()) {
        throw new MojoExecutionException(
            "Unwanted dependencies found. See output for details.");
      }

    } catch (final IOException e) {
      throw new MojoExecutionException(e.getMessage(), e);
    } catch (ClassNotFoundException e) {
      throw new MojoExecutionException("Could not find result renderer class " + resultRenderer, e);
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

  public Class<? extends ResultRenderer> getResultRenderer() {
    return resultRendererClass;
  }

  public String getOutputFile() {
    if(outputFile != null) {
      return outputFile;
    }

    if(XMLResultRenderer.class.isAssignableFrom(resultRendererClass)) {
      return XML_RESULT_FILE;
    }

    return TEXT_RESULT_FILE;
  }
}