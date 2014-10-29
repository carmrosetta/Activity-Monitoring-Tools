package ing.unipi.it.activitymonitoringtools;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;
import java.util.List;

public class SensorDataLogService extends SensorService implements SensorEventListener {

    public static final String TAG = SensorDataLogService.class.getName();

    private PowerManager.WakeLock mWakeLock = null;

    int activeSensors = 0;

    File[] samplesDirectories;
    File[] samplesFiles;

    //SensorManager sensorManager;

    private NotificationManager notificationManager;

    String smartPhonePosition;
   // public List<SensorInfo> selectedSensorsData;

    //ActionScreenOffReceiver actionScreenOffReceiver;



    public SensorDataLogService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        actionScreenOffReceiver = new ActionScreenOffReceiver(sensorManager, this);

        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        registerReceiver(actionScreenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Sensor data log service started", "service started");

        Bundle extras = intent.getExtras();
        smartPhonePosition = (String) extras.get("SmartPhone position");
        selectedSensorsData = (List<SensorInfo>) extras.getSerializable("Selected sensors");



        registerListeners();

        mWakeLock.acquire();

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(actionScreenOffReceiver);

        unregisterListener();

        mWakeLock.release();

        //notificationManager.cancelAll();
        super.onDestroy();
    }

    public void registerListeners() {

        for(SensorInfo s : selectedSensorsData) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(s.getSensorType()), s.getSensorSpeed());
        }

        Log.e("Listeners registered", "listeners registered");
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

   @Override
    public void onSensorChanged(SensorEvent event) {

       Log.e("sampled",""+event.timestamp);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
