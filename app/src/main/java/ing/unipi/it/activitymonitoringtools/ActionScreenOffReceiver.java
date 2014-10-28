package ing.unipi.it.activitymonitoringtools;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

public class ActionScreenOffReceiver extends BroadcastReceiver {

    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;

    SensorManager sensorManager;
    SensorEventListener sensorEventListener;
    List<SensorInfo> activeSensors;
    NotificationManager notificationManager;
    Class cls;



    public ActionScreenOffReceiver() {
    }

    public ActionScreenOffReceiver(SensorManager sensorManager, SensorEventListener sensorEventListener, List<SensorInfo> activeSensors, NotificationManager notificationManager, Class cls) {
        this.sensorManager = sensorManager;
        this.sensorEventListener = sensorEventListener;
        this.activeSensors = activeSensors;
        this.notificationManager = notificationManager;
        this.cls = cls;
    }



    @Override
    public void onReceive(final Context context, Intent intent) {

        if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                sensorManager.unregisterListener(sensorEventListener);

                for(SensorInfo s : activeSensors) {
                    sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(s.getSensorType()), s.getSensorSpeed());
                }

                notificationManager.cancelAll();
                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Utilities.showNotification(context, notificationManager,"Service Running" ,cls );


            }
        };
    }
}
