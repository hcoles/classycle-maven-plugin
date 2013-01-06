package org.pitest.classycle.analyse;

import org.pitest.classycle.Project;

public interface AnalyseProject extends Project {

  String getTitle();

  boolean isPackagesOnly();
}
