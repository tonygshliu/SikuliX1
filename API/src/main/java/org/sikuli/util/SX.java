package org.sikuli.util;

import org.sikuli.basics.Debug;

import java.awt.GraphicsEnvironment;

public class SX {
  public static boolean isNull(Object obj) {
    return null == obj;
  }

  public static boolean isNotNull(Object obj) {
    return null != obj;
  }

  public static boolean allNotNull(Object... objs) {
    boolean retVal = true;
    for (Object obj : objs) {
      retVal &= null != obj;
    }
    return retVal;
  }

  public static boolean isNotSet(Object obj) {
    if (null != obj && obj instanceof String) {
      if (((String) obj).isEmpty()) {
        return true;
      } else {
        return false;
      }
    }
    return null == obj;
  }

  public static boolean isSet(Object obj) {
    if (null != obj && obj instanceof String) {
      if (((String) obj).isEmpty()) {
        return false;
      } else {
        return true;
      }
    }
    return null != obj;
  }

  public static boolean isSet(String var, String val) {
    if (null != var && null != val) {
      if (var.isEmpty()) {
        return false;
      } else {
        return val.equals(var);
      }
    }
    return false;
  }

  public static void pause(int time) {
    try {
      Thread.sleep(time * 1000);
    } catch (InterruptedException ex) {
    }
  }

  public static void pause(float time) {
    try {
      Thread.sleep((int) (time * 1000));
    } catch (InterruptedException ex) {
    }
  }

  public static void pause(double time) {
    try {
      Thread.sleep((int) (time * 1000));
    } catch (InterruptedException ex) {
    }
  }

  public static String str(Object... args) {
    String result = "";
    int nparms = -1;
    Object[] parms = null;
    for (Object arg : args) {
      if (nparms < 0) {
        if (arg instanceof String) {
          if (!"#".equals(((String) arg))) {
            result += ((String) arg).replaceAll("'", "\"") + "\n";
            nparms -= 1;
          } else {
            parms = new Object[args.length + nparms];
            nparms = 0;
          }
        }
      } else {
        parms[nparms++] = arg;
      }
    }
    if (nparms > 0) {
      try {
        result = String.format(result, parms);
      } catch (Exception ex) {
        Debug.error("SX.str: %s [[%s]]", ex.getMessage(), result);
      }
    }
    return result;
  }  /**
   * checks, whether Java runs with a valid GraphicsEnvironment (usually means real screens connected)
   *
   * @return false if Java thinks it has access to screen(s), true otherwise
   */
  public static boolean isHeadless() {
    return GraphicsEnvironment.isHeadless();
  }

  public static boolean isTravisCI() {
    return SX.isSet(System.getenv("TRAVIS"), "true");
  }


}
