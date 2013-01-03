package org.pitest.classycle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class Unzip {

  private final StreamFactory sf;
  
  Unzip(StreamFactory sf) {
    this.sf = sf;
  }
  
  void extractZipFile(final InputStream is) throws IOException {

    final byte[] buffer = new byte[1024];
    final ZipInputStream zis = new ZipInputStream(is);
    ZipEntry ze = zis.getNextEntry();

    while (ze != null) {
      if (!ze.isDirectory()) {
        writeEntry(buffer, zis, ze);
      }
      ze = zis.getNextEntry();
    }

    zis.closeEntry();
    zis.close();
  }

  private void writeEntry(
      final byte[] buffer, final ZipInputStream zis, final ZipEntry ze)
      throws FileNotFoundException, IOException {
    final OutputStream fos = sf.createStream(ze.getName().replace("/", File.separator));

    int len;
    while ((len = zis.read(buffer)) > 0) {
      fos.write(buffer, 0, len);
    }

    fos.close();
  }
}