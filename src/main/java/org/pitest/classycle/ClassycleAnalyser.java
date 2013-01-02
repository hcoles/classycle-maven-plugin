package org.pitest.classycle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import classycle.Analyser;
import classycle.util.AndStringPattern;
import classycle.util.NotStringPattern;
import classycle.util.StringPattern;
import classycle.util.TrueStringPattern;
import classycle.util.WildCardPattern;

public class ClassycleAnalyser {

  private final Project project;

  ClassycleAnalyser(final Project project) {
    this.project = project;
  }

  public void analyse() throws IOException {

    final File classDir = new File(this.project.getOutputDirectory());

    final Analyser analyser = createAnalyser(
        new String[] { classDir.getAbsolutePath() }, getPattern(),
        getReflectionPattern(), this.project.isMergeInnerClasses());
    analyser.readAndAnalyse(this.project.isPackagesOnly());
    writeReport(analyser);

  }

  private void writeReport(final Analyser analyser) throws IOException {
    final File f = new File(this.project.getTargetDirectory(), "classycle.xml");
    final PrintWriter writer = new PrintWriter(createWriter(f));
    analyser.printXML("Cycles", this.project.isPackagesOnly(), writer);
    writer.close();
  }

  private StringPattern getReflectionPattern() {
    return listToWildCard(this.project.getReflectionPatterns());
  }

  private static StringPattern listToWildCard(final List<String> patterns) {
    if ((patterns == null) || patterns.isEmpty()) {
      return new TrueStringPattern();
    }
    return WildCardPattern.createFromsPatterns(StringUtils.join(patterns, ","),
        ",");
  }

  protected StringPattern getPattern() {
    final AndStringPattern pattern = new AndStringPattern();
    pattern.appendPattern(listToWildCard(this.project.getIncludingClasses()));
    if (this.project.getExcludingClasses() != null) {
      pattern.appendPattern(new NotStringPattern(listToWildCard(this.project
          .getExcludingClasses())));
    }
    return pattern;
  }

  private Analyser createAnalyser(final String[] classes,
      final StringPattern pattern, final StringPattern reflectionPattern,
      final boolean mergeInnerClasses) {
    return new Analyser(classes, getPattern(), getReflectionPattern(),
        this.project.isMergeInnerClasses());
  }

  Writer createWriter(final File file) throws IOException {
    return new FileWriter(file);
  }
}
