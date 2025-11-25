package pl.fulllegitcode.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class TemperatureReceiver extends BroadcastReceiver {

  private float _temperature;
  public float temperature() { return _temperature; }

  public TemperatureReceiver(Activity activity) {
    activity.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
    _temperature = (float) temp / 10;
  }

}
