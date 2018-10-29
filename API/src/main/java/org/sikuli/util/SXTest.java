/*
 * Copyright (c) 2017 - sikulix.com - MIT license
 */

package org.sikuli.util;

import org.sikuli.script.ImagePath;
import org.sikuli.script.Key;
import org.sikuli.script.RunTime;

import java.io.File;

public class SXTest extends SXError {
  public static String defaultImagePath = "SX_Images";
  public static String jarImagePathDefault = "." + "/" + defaultImagePath;
  public static String jarImagePathClass = "com.sikulix.testjar.Testjar" + "/" + defaultImagePath;
  public static String mavenRoot = "target/classes";
  public static String gitRoot = "https://raw.githubusercontent.com/RaiMan/SikuliX2/master";
  public static String gitImagePath = gitRoot + "/src/main/resources/" + defaultImagePath;
  public static String imageNameDefault = "sikulix2";
  public static String imageNameGoogle = "google";

  private boolean local = false;

  public SXTest() {
    String name = getMethodName();
    if (name.endsWith("_l")) {
      local = true;
    }
    SikulixTest.p("***** starting %s", name);
    ImagePath.setBundlePath(new File(RunTime.get().fWorkDir, SikulixTest.showBase).getAbsolutePath());
  }

  @Override
  public boolean shouldNotRun() {
    if (super.shouldNotRun()) {
      return true;
    }
    if (local && SX.isTravisCI()) {
      itShouldNotRun = "TravisCI: should not run";
      after();
      return true;
    }
    return false;
  }

  private String result = "";

  public void setResult(String text, Object... args) {
    result = String.format(text, args);
  }

  public void addResult(String text, Object... args) {
    result += " " + String.format(text, args);
  }

  public String failed() {
    return failed("no success: %s", result);
  }

  public String failed(String msg, Object... args) {
    return String.format(msg, args);
  }

  public void after() {
    SikulixTest.p("***** ending %s", toString());
  }

  @Override
  public String toString() {
    return String.format("%s (%s)", methodName, shouldNotRun() ? itShouldNotRun : result);
  }
}
