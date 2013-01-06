package org.pitest.classycle;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import classycle.Analyser;
import classycle.util.AndStringPattern;
import classycle.util.NotStringPattern;
import classycle.util.StringPattern;
import classycle.util.TrueStringPattern;
import classycle.util.WildCardPattern;

public abstract class AbstractGoal<T extends Project> {

  protected final T             project;
  protected final StreamFactory streamFactory;

  public AbstractGoal(final T project, final StreamFactory streamFactory) {
    this.project = project;
    this.streamFactory = streamFactory;
  }

  public final boolean analyse() throws IOException {
    final File classDir = new File(this.project.getOutputDirectory());

    final Analyser analyser = createAnalyser(
        new String[] { classDir.getAbsolutePath() }, getPattern(),
        getReflectionPattern(), this.project.isMergeInnerClasses());

    return analyse(analyser);

  }

  /**
   * 
   * @param analyser
   * @return False if the build should be reported as broken
   * @throws IOException
   */
  protected abstract boolean analyse(Analyser analyser) throws IOException;

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

  private StringPattern getPattern() {
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

}
