package org.pitest.classycle.check;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Properties;

import org.pitest.classycle.AbstractGoal;
import org.pitest.classycle.StreamFactory;

import classycle.Analyser;
import classycle.dependency.DependencyChecker;
import classycle.dependency.ResultRenderer;

class CheckGoal extends AbstractGoal<CheckProject> {

  CheckGoal(final CheckProject project, final StreamFactory streamFactory) {
    super(project, streamFactory);
  }

  @Override
  public boolean analyse(final Analyser analyser) throws IOException {

    final Properties properties = System.getProperties();
    final DependencyChecker dependencyChecker = new DependencyChecker(analyser,
        this.project.getDependencyDefinition(), properties, getRenderer());
    final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
        this.streamFactory.createStream(project.getOutputFile()), project.getReportEncoding()));
    try {
      if (!dependencyChecker.check(printWriter)) {
        return !this.project.isFailOnUnWantedDependencies();
      }
      return true;
    } finally {
      printWriter.flush();
    }
  }

  private ResultRenderer getRenderer() {
    try {
      return project.getResultRenderer().newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException("Could not instantiate " + project.getResultRenderer(), e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Could not access " + project.getResultRenderer(), e);
    }
  }

}
