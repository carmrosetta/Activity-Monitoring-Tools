package ing.unipi.it.activitymonitoringtools;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import java.util.List;

public class ActionScreenOffReceiver extends BroadcastReceiver {

    public final static int SCREEN_OFF_RECEIVER_DELAY = 500;

    private SensorManager sensorManager;
    SensorEventListener sensorEventListener;


    public ActionScreenOffReceiver() {
    }

    public ActionScreenOffReceiver(SensorManager sensorManager, SensorEventListener sensorEventListener) {
        this.sensorManager = sensorManager;
        this.sensorEventListener = sensorEventListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("screen off event", "the screen went off ");


                reactivateListeners();
             //   notificationManager.cancelAll();
             //   notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
             //   Utilities.showNotification(getApplicationContext(), notificationManager, "Service running", MainActivity.class);

            }
        };

        new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);

    }

    public void reactivateListeners() {
        sensorManager.unregisterListener(sensorEventListener);
        for(SensorInfo s : /*SensorDataLogService.selectedSensorsData*/((SensorService)sensorEventListener).selectedSensorsData) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(s.getSensorType()), s.getSensorSpeed());
        }

        Log.e("Listeners registered", "listeners registered from broadcast receiver "+((SensorDataLogService)sensorEventListener).selectedSensorsData.size());
    }

}
