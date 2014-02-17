package org.pitest.classycle.check;

import java.io.IOException;

import org.pitest.classycle.Project;

import classycle.dependency.ResultRenderer;

public interface CheckProject extends Project {
  String getDependencyDefinition() throws IOException;

  boolean isFailOnUnWantedDependencies();

  Class<? extends ResultRenderer> getResultRenderer();

  String getOutputFile();
}
