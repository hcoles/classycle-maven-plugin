package org.pitest.classycle.check;

import java.io.IOException;

import org.pitest.classycle.Project;

public interface CheckProject extends Project {
  String getDependencyDefinition() throws IOException;

  boolean isFailOnUnWantedDependencies();
}
