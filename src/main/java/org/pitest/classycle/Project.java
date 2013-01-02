package org.pitest.classycle;

import java.util.List;

public interface Project {

  boolean isMergeInnerClasses();

  String getOutputDirectory();

  boolean isPackagesOnly();

  String getTargetDirectory();

  List<String> getReflectionPatterns();

  List<String> getIncludingClasses();

  List<String> getExcludingClasses();

}
