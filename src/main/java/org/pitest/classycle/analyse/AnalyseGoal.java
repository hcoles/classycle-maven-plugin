package org.pitest.classycle.analyse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.pitest.classycle.AbstractGoal;
import org.pitest.classycle.StreamFactory;

import classycle.Analyser;

class AnalyseGoal extends AbstractGoal<AnalyseProject> {

  static final String XML_FILE = "classycle.xml";

  AnalyseGoal(final AnalyseProject project, final StreamFactory streamFactory) {
    super(project, streamFactory);
  }

  @Override
  public boolean analyse(final Analyser analyser) throws IOException {
    analyser.readAndAnalyse(this.project.isPackagesOnly());
    writeReport(analyser);
    return true;
  }

  private void writeReport(final Analyser analyser) throws IOException {
    final PrintWriter writer = new PrintWriter(
        this.streamFactory.createStream(XML_FILE));
    analyser.printXML(this.project.getTitle(), this.project.isPackagesOnly(),
        writer);
    writer.close();

    copyResourceForHtmlViewing();

  }

  private void copyResourceForHtmlViewing() throws IOException {
    final InputStream zipFileStream = Thread.currentThread()
        .getContextClassLoader().getResourceAsStream("resources.zip");
    final Unzip uz = new Unzip(this.streamFactory);
    uz.extractZipFile(zipFileStream);
  }

}
