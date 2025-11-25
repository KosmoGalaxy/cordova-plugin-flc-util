package pl.fulllegitcode.utilcordova;

import android.app.Activity;
import android.view.WindowManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import pl.fulllegitcode.util.Exo;
import pl.fulllegitcode.util.Util;

public class UtilCordova extends CordovaPlugin {
  public static final String ACTION_ACQUIRE_WAKE_LOCK = "acquireWakeLock";
  public static final String ACTION_RELEASE_WAKE_LOCK = "releaseWakeLock";
  public static final String ACTION_SET_KEEP_SCREEN_ON = "setKeepScreenOn";
  public static final String ACTION_DECODE_IMAGE = "decodeImage";
  public static final String ACTION_GET_IP = "getIp";
  public static final String ACTION_EXO_CREATE = "exoCreate";
  public static final String ACTION_EXO_DISPOSE = "exoDispose";

  private final ArrayList<Exo> exos = new ArrayList<Exo>();

  @Override
  public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
    Activity activity = cordova.getActivity();
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
    if (action.equals(ACTION_EXO_CREATE)) {
      activity.runOnUiThread(() -> {
        Exo exo = null;
        try {
          ExecutorService threadPool = cordova.getThreadPool();
          String uri = args.getString(0);
          exo = new Exo(activity, threadPool, uri, new Exo.Callback() {
            @Override
            public void onError(int error) {
              try {
                JSONObject event = new JSONObject();
                event.put("type", "error");
                event.put("error", error);
                PluginResult result = new PluginResult(PluginResult.Status.OK, event);
                result.setKeepCallback(true);
                callbackContext.sendPluginResult(result);
              } catch (JSONException ignored) {
              }
            }

            @Override
            public void onPlaybackState(int state) {
              try {
                JSONObject event = new JSONObject();
                event.put("type", "playbackState");
                event.put("state", state);
                PluginResult result = new PluginResult(PluginResult.Status.OK, event);
                result.setKeepCallback(true);
                callbackContext.sendPluginResult(result);
              } catch (JSONException ignored) {
              }
            }
          });
          exos.add(exo);
          JSONObject event = new JSONObject();
          event.put("type", "create");
          event.put("id", exo.id());
          PluginResult result = new PluginResult(PluginResult.Status.OK, event);
          result.setKeepCallback(true);
          callbackContext.sendPluginResult(result);
        } catch (Exception e) {
          if (exo != null) {
            exo.dispose();
            exos.remove(exo);
          }
          callbackContext.error(e.getMessage());
        }
      });
      return true;
    }
    if (action.equals(ACTION_EXO_DISPOSE)) {
      Exo exo = getExo(args.getInt(0));
      if (exo != null) {
        exo.dispose();
        exos.remove(exo);
        callbackContext.success();
      } else {
        callbackContext.error("exo not found");
      }
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

  private Exo getExo(int id) {
    for (int i = 0; i < exos.size(); i++) {
      Exo exo = exos.get(i);
      if (exo.id() == id)
        return exo;
    }
    return null;
  }
}
