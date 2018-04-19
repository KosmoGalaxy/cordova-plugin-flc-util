package pl.fulllegitcode.util;

import android.app.Activity;
import android.app.Fragment;

public class RequestPermissionsDelegate {

  interface InnerCallback {
    void run(Activity activity, int requestCode);
    void run(Fragment fragment, int requestCode);
    void onRequestPermissionsResult(String[] permissions, int[] grantResults);
  }


  private static int _nextRequestCode = 1;


  private int _requestCode = _nextRequestCode++;
  public int requestCode() { return _requestCode; }

  private InnerCallback _innerCallback;
  private InnerCallback innerCallback() { return _innerCallback; }

  RequestPermissionsDelegate(InnerCallback innerCallback) {
    _innerCallback = innerCallback;
  }

  public void run(Activity activity) {
    innerCallback().run(activity, requestCode());
  }

  public void run(Fragment fragment) {
    innerCallback().run(fragment, requestCode());
  }

  public void onRequestPermissionsResult(String[] permissions, int[] grantResults) {
    innerCallback().onRequestPermissionsResult(permissions, grantResults);
  }

}
