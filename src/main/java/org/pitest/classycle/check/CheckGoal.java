package org.pitest.classycle.check;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.pitest.classycle.AbstractGoal;
import org.pitest.classycle.StreamFactory;

import classycle.Analyser;
import classycle.dependency.DefaultResultRenderer;
import classycle.dependency.DependencyChecker;
import classycle.dependency.ResultRenderer;

class CheckGoal extends AbstractGoal<CheckProject> {

  static final String RESULTS_FILE = "checkresults.txt";

  CheckGoal(final CheckProject project, final StreamFactory streamFactory) {
    super(project, streamFactory);
  }

  @Override
  public boolean analyse(final Analyser analyser) throws IOException {

    final Properties properties = System.getProperties();
    final DependencyChecker dependencyChecker = new DependencyChecker(analyser,
        this.project.getDependencyDefinition(), properties, getRenderer());
    final PrintWriter printWriter = new PrintWriter(
        this.streamFactory.createStream(RESULTS_FILE));
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
    return new DefaultResultRenderer();
  }

}
