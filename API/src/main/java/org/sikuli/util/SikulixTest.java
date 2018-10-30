/*
 * Copyright (c) 2010-2018, sikuli.org, sikulix.com - MIT license
 */

package org.sikuli.util;

import org.sikuli.script.*;

import java.util.Date;
import java.util.List;

public class SikulixTest {

  //<editor-fold desc="housekeeping">
  private static Screen scr = new Screen();

  private static long start = -1;

  private static void timer() {
    start = new Date().getTime();
  }

  private static void timer(String msg) {
    p("%d (%s)", new Date().getTime() - start, msg.isEmpty() ? "msec" : msg);
  }

  public static void p(String msg, Object... args) {
    if (msg.isEmpty()) {
      return;
    }
    System.out.println(String.format(msg, args));
  }

  private static void error(String msg, Object... args) {
    p("[ERROR]" + msg, args);
  }

  public static String showBase = "src/main/resources/ImagesAPI";
  private static String showLink;
  private static int showWait;
  private static int showBefore;
  private static boolean isShown = false;

  public static void show(String image) {
    show(image, 0, 0);
  }

  public static void show(String image, int wait) {
    show(image, wait, 0);
  }

  public static void show(String image, int wait, int before) {
    if (!image.endsWith(".png")) {
      image += ".png";
    }
    showLink = "file://" + Image.create(image).getFileURL().getPath();
    showWait = wait;
    showBefore = before;
    Thread runnable = new Thread() {
      @Override
      public void run() {
        if (before > 0) {
          RunTime.pause(showBefore);
        }
        App.openLink(showLink);
        if (wait > 0) {
          RunTime.pause(showWait);
          //p("%s", App.focusedWindow());
          scr.type("w", Key.CMD);
        } else {
          isShown = true;
        }
      }
    };
    runnable.start();
  }

  public static void showStop() {
    if (isShown) {
      scr.type("w", keyMeta);
      isShown = false;
    }
  }

  public static Region reg = null;
  public static Region regWin = null;

  public static boolean openTestPage() {
    return openTestPage("");
  }

  public static String keyMeta = Key.CTRL;
  private static boolean isBrowserRunning = false;

  public static boolean openTestPage(String page) {
    String testPageBase = "https://github.com/RaiMan/SikuliX1/wiki/";
    String testPage = "Test-page-text";
    if (!page.isEmpty()) {
      testPage = page;
    }
    String actualPage = testPageBase + testPage;
    boolean success = false;
    String corner = "apple";
    Pattern pCorner = new Pattern(corner).similar(0.9);
    Match cornerSeen = null;
    if (App.openLink(actualPage)) {
      scr.wait(1.0);
      Screen allScreen = Screen.all();
      if (Do.SX.isNotNull(allScreen.exists(pCorner, 30))) {
        success = true;
        cornerSeen = allScreen.getLastMatch();
        cornerSeen.hover();
        reg = App.focusedWindow();
        regWin = new Region(reg);
      }
    }
    if (success) {
      int wheelDirection = 0;
      success = false;
      while (!success) {
        List<Match> matches = reg.getAll(corner);
        if (matches.size() == 2) {
          reg = matches.get(0).union(matches.get(1));
          reg.h += 5;
          success = true;
          break;
        }
        if (wheelDirection == 0) {
          wheelDirection = Button.WHEEL_DOWN;
          reg.wheel(wheelDirection, 1);
          scr.wait(0.5);
          Match cornerMatch = regWin.exists(pCorner);
          if (cornerMatch.y >= cornerSeen.y) {
            wheelDirection *= -1;
          }
        }
        reg.wheel(wheelDirection, 1);
        scr.wait(0.5);
      }
    }
    if (!success) {
      p("***** Error: web page did not open (30 secs)");
    } else {
      //reg.highlight(1);
      isBrowserRunning = true;
    }
    return success;
  }

  private static void browserStop() {
    if (isBrowserRunning) {
      scr.type("w", keyMeta);
    }
    isBrowserRunning = false;
  }

  public SXTest current;

  public void after() {
    current.after();
    ScreenHighlighter.closeAll();
    showStop();
    browserStop();
  }

  public static List<Match> highlight(List<Match> regs, int time) {
    for (Match reg : regs) {
      reg.highlight();
    }
    scr.wait(time * 1.0);
    ScreenHighlighter.closeAll();
    return regs;
  }

  public static void highlight(List<Match> regs) {
    highlight(regs, 1);
  }

  public static Region highlightIf(Region match) {
    if (null != match) {
      match.highlight(2);
    }
    return match;
  }
  //</editor-fold>
}
