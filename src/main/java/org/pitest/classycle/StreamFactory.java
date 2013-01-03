package org.pitest.classycle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class StreamFactory {
  
  private final String outputFolder;
  
  StreamFactory(String outputFolder) {
    this.outputFolder = outputFolder;
  }
  
  public OutputStream createStream(String fileName) throws IOException {
    final File f = new File(outputFolder + File.separator + fileName);
    f.getParentFile().mkdirs();
    return new FileOutputStream(f);
  }

}
