/*
 * Copyright (c) 2010-2018, sikuli.org, sikulix.com - MIT license
 */

package org.sikuli.android;


import org.sikuli.basics.Debug;
import org.sikuli.script.RunTime;
import se.vidstige.jadb.AdbServerLauncher;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.Subprocess;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by tg44 on 2016. 06. 26..
 * Modified by RaiMan
 */
public class ADBClient {

  private static JadbConnection jadb = null;
  private static boolean shouldStopServer = false;
  private static JadbDevice device = null;
  public static boolean isAdbAvailable = false;
  public static String adbExec = "platform-tools/adb";
  private static String adbFilePath = "adb";

  private static void init(String adbWhereIs) {
    //getConnection(true);
    String adbPath;
    if (jadb == null) {
      if (adbWhereIs == null || adbWhereIs.isEmpty()) {
        if (RunTime.get().runningWindows) adbExec += ".exe";
        adbPath = System.getenv("sikulixadb");
        if (adbPath == null) {
          adbPath = System.getProperty("sikulixadb");
        }
        if (adbPath == null) {
          adbPath = RunTime.get().fWorkDir.getAbsolutePath();
        }
        File adbFile = new File(adbPath, adbExec);
        if (!adbFile.exists()) {
          adbFile = new File(adbPath);
        }
        adbFilePath = adbFile.getAbsolutePath();
      } else {
        adbFilePath = adbWhereIs;
      }
      try {
        new AdbServerLauncher(new Subprocess(), adbFilePath).launch();
        getConnection(false);
        if (jadb != null) {
          isAdbAvailable = true;
          shouldStopServer = true;
          Debug.log(3, "ADBClient: ADBServer started");
        } else {
          reset();
        }
      } catch (Exception e) {
        //Cannot run program "adb": error=2, No such file or directory
        if (e.getMessage().startsWith("Cannot run program")) {
          Debug.error("ADBClient: package adb not available: %s", adbFilePath);
        } else {
          Debug.error("ADBClient: ADBServer problem: %s", e.getMessage());
        }
      }
    }
    String serial = null;
    if (jadb != null) {
      List<JadbDevice> devices = null;
      try {
        devices = jadb.getDevices();
      } catch (Exception e) {
      }
      if (devices != null && devices.size() > 0) {
        device = devices.get(0);
        serial = device.getSerial();
      } else {
        device = null;
        Debug.error("ADBClient: init: no devices attached");
      }
    }
    if (device != null) {
      Debug.log(3, "ADBClient: init: attached device: serial(%s)", serial);
    }
  }

  public static void reset() {
    device = null;
    jadb = null;
    if (!shouldStopServer) {
      return;
    }
    try {
      Process p = Runtime.getRuntime().exec(new String[] {adbFilePath, "kill-server"});
      p.waitFor();
      Debug.log(3,"ADBClient: ADBServer should be stopped now");
    } catch (Exception e) {
      Debug.error("ADBClient: reset: kill-server did not work");
    }
  }

  private static void getConnection(boolean quiet) {
    if (jadb == null) {
      try {
        jadb = new JadbConnection();
        jadb.getHostVersion();
        Debug.log(3, "ADBClient: ADBServer connection established");
      } catch (Exception e) {
        if (!quiet) {
          Debug.error("ADBClient: ADBServer connection not possible: %s", e.getMessage());
        }
        jadb = null;
      }
    }
  }

  public static JadbDevice getDevice(String adbExec) {
    init(adbExec);
    return device;
  }

  //TODO: get device by id

  public boolean isValid() {
    return jadb != null;
  }

  public boolean hasDevices() {
    return device != null;
  }
}
