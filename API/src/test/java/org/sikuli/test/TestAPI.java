/*
 * Copyright (c) 2017 - sikulix.com - MIT license
 */

package org.sikuli.test;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.sikuli.basics.Debug;
import org.sikuli.basics.Settings;
import org.sikuli.script.*;
import org.sikuli.util.SX;
import org.sikuli.util.SXTest;
import org.sikuli.util.SikulixTest;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAPI extends SikulixTest {

  @BeforeClass
  public static void setUpClass() {
    Debug.on(0);
    Settings.ActionLogs = false;
    Settings.InfoLogs = false;
    if (RunTime.get().runningMac) {
      SikulixTest.keyMeta = Key.CMD;
    }
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Ignore
  public void test_000_template() {
    current = new SXTest();
    if (current.shouldNotRun()) {
      return;
    }
    String testImage = "findBase";
    Screen scr = new Screen();
    current.setResult("test template");
    after();
  }

  @Test
  public void test_001_base() {
    current = new SXTest();
    if (current.shouldNotRun()) {
      return;
    }
    Screen scr = new Screen();
    String testImage = "findBase";
    show(testImage);
    scr.wait(2.0);
    current.setResult("ok: " + highlightIf(scr.exists(testImage)));
    after();
  }

  @Test
  public void test_002_findChanges() {
    current = new SXTest();
    if (current.shouldNotRun()) {
      return;
    }
    String testImage = "findBase";
    Screen scr = new Screen();
    show(testImage);
    scr.wait(2.0);
    Finder finder = new Finder(testImage);
    String imgChange = "findChange3";
    List<Region> changes = finder.findChanges(imgChange);
    Match match = scr.exists(testImage, 10);
    if (SX.isNotNull(match)) {
      for (Region change : changes) {
        match.getInset(change).highlight(1);
      }
    }
    String expected = "should be 5 changes";
    assert changes.size() == 5 : expected;
    current.setResult("ok: " + expected);
    after();
  }

}
