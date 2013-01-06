package org.pitest.classycle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StreamFactory {

  private final String outputFolder;

  public StreamFactory(final String outputFolder) {
    this.outputFolder = outputFolder;
  }

  public OutputStream createStream(final String fileName) throws IOException {
    final File f = new File(this.outputFolder + File.separator + fileName);
    f.getParentFile().mkdirs();
    return new FileOutputStream(f);
  }

}
