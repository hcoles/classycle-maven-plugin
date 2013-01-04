package org.pitest.classycle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import classycle.Analyser;
import classycle.util.AndStringPattern;
import classycle.util.NotStringPattern;
import classycle.util.StringPattern;
import classycle.util.TrueStringPattern;
import classycle.util.WildCardPattern;

public class ClassycleAnalyser {

  public static final String XML_FILE = "classycle.xml";
  
  private final Project project;
  private final StreamFactory streamFactory;

  ClassycleAnalyser(final Project project, StreamFactory streamFactory) {
    this.project = project;
    this.streamFactory = streamFactory;
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
    final PrintWriter writer = new PrintWriter(streamFactory.createStream(XML_FILE));
    analyser.printXML(this.project.getTitle(), this.project.isPackagesOnly(), writer);
    writer.close();
 
    copyResourceForHtmlViewing();
    
  }

  private void copyResourceForHtmlViewing() throws IOException {
    InputStream zipFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("resources.zip");
    Unzip uz = new Unzip(streamFactory);
    uz.extractZipFile(zipFileStream);
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

}
