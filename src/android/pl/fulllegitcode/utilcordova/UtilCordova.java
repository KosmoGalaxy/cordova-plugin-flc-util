package pl.fulllegitcode.utilcordova;

import android.view.WindowManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

import pl.fulllegitcode.util.Util;

public class UtilCordova extends CordovaPlugin {

  public static final String ACTION_ACQUIRE_WAKE_LOCK = "acquireWakeLock";
  public static final String ACTION_RELEASE_WAKE_LOCK = "releaseWakeLock";
  public static final String ACTION_SET_KEEP_SCREEN_ON = "setKeepScreenOn";
  public static final String ACTION_DECODE_IMAGE = "decodeImage";
  public static final String ACTION_GET_IP = "getIp";

  @Override
  public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
    if (action.equals(ACTION_ACQUIRE_WAKE_LOCK)) {
      _acquireWakeLockThread(!args.isNull(0) ? args.getInt(0) : Util.WAKE_LOCK_TIMEOUT, callbackContext);
      return true;
    }
    if (action.equals(ACTION_RELEASE_WAKE_LOCK)) {
      _releaseWakeLockThread(callbackContext);
      return true;
    }
    if (action.equals(ACTION_SET_KEEP_SCREEN_ON)) {
      _setKeepScreenOn(args.getBoolean(0), callbackContext);
      return true;
    }
    if (action.equals(ACTION_DECODE_IMAGE)) {
      _decodeImage(args.getArrayBuffer(0), callbackContext);
      return true;
    }
    if (action.equals(ACTION_GET_IP)) {
      callbackContext.success(Util.getIp(cordova.getActivity()));
      return true;
    }
    return false;
  }

  @Override
  public void onDestroy() {
    Util.releaseWakeLock();
    super.onDestroy();
  }

  private void _acquireWakeLockThread(final int timeout, final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        String error = Util.acquireWakeLock(timeout, cordova.getActivity());
        if (error != null) {
          callbackContext.error(error);
          return;
        }
        callbackContext.success();
      }
    });
  }

  private void _releaseWakeLockThread(final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        String error = Util.releaseWakeLock();
        if (error != null) {
          callbackContext.error(error);
          return;
        }
        callbackContext.success();
      }
    });
  }

  private void _setKeepScreenOn(final boolean value, final CallbackContext callbackContext) {
    cordova.getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (value) {
          cordova.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
          cordova.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        callbackContext.success();
      }
    });
  }

  private void _decodeImage(final byte[] bytes, final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        callbackContext.success(Util.decodeImage(bytes));
      }
    });
  }

}
