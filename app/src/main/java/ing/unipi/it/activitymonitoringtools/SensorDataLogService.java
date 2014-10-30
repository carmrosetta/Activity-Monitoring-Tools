package ing.unipi.it.activitymonitoringtools;

import android.app.NotificationManager;
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
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class SensorDataLogService extends SensorService implements SensorEventListener {

    public static final String TAG = SensorDataLogService.class.getName();

    private PowerManager.WakeLock mWakeLock = null;

    int activeSensors = 0;

    File[] samplesDirectories;
    File[] samplesFiles;


    private NotificationManager notificationManager;

    String smartPhonePosition;

    //SensorManager sensorManager;
    //public List<SensorInfo> selectedSensorsData;
    //ActionScreenOffReceiver actionScreenOffReceiver;



    public SensorDataLogService() {
        super();
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
        smartPhonePosition = (String) extras.get("Smartphone position");
        selectedSensorsData = (List<SensorInfo>) extras.getSerializable("Selected sensors");
        activeSensors = selectedSensorsData.size();

        samplesDirectories = new File[activeSensors];
        samplesFiles = new File[activeSensors];

        long now = System.currentTimeMillis();

        String generalHeader = getGeneralHeader(now, smartPhonePosition);
       // Toast.makeText(getApplicationContext(), generalHeader, Toast.LENGTH_LONG).show();

        for(int i=0; i < selectedSensorsData.size(); i++) {

            String sensorHeader = getSensorSpecificHeader(selectedSensorsData.get(i).getSensorType(), selectedSensorsData.get(i));
            String relationHeader = getRelationHeader(selectedSensorsData.get(i).getSensorType());

            //creazione file e directories
            String sensorName = Utilities.getSensorNameById(selectedSensorsData.get(i).getSensorType(), selectedSensorsData.get(i).getSensorName());
            samplesDirectories[i] = Utilities.createDirectory("SensorDataLog/Samples/"+sensorName+
                    "/"+Utilities.getDateTimeFromMillis(now, "yy-MM-dd"));
            samplesFiles[i] = Utilities.createFile(samplesDirectories[i],Utilities.getDateTimeFromMillis(now, "kk-mm")+".arff");


            Utilities.writeData(samplesFiles[i], sensorHeader);
            Utilities.writeData(samplesFiles[i], generalHeader);
            Utilities.writeData(samplesFiles[i], relationHeader);

            }



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

      // Log.e("sampled",""+event.timestamp);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    public String getSensorSpecificHeader(int sensorId, SensorInfo sensorInfo) {

        String sensorName = Utilities.getSensorNameById(sensorId, sensorInfo.getSensorName());
        float sensorRange = sensorInfo.getMaxRange();
        String sensorSpeed = Utilities.getAndroidSamplingRateById(sensorInfo.getSensorSpeed());


        String sensorHeader = "% "+sensorName+" Track\n%\n"+
                              "% Range: "+sensorRange+" "+Utilities.getSensorUnitById(sensorInfo.getSensorType())+"\n"+
                              "% Android Sampling Rate : "+sensorSpeed+"\n";
        return sensorHeader;
    }

    public String getGeneralHeader(long currentTimeInMillis, String smartphonePosition) {

        String startDate = Utilities.getDateTimeFromMillis(currentTimeInMillis, "yy-MM-dd");
        String startTime = Utilities.getDateTimeFromMillis(currentTimeInMillis, "kk-mm-ss");
        String device = Utilities.getDeviceName();
        String androidVersion = Utilities.getAndroidVersion();

        UserInformationManager userInformationManager = new UserInformationManager(this);
        HashMap<String, String> userInfo = userInformationManager.getUserInformation();
        String userSex = userInfo.get(UserInformationManager.USER_SEX);
        String userAge = userInfo.get(UserInformationManager.USER_AGE);
        String userHeight = userInfo.get(UserInformationManager.USER_HEIGHT);
        String userWeight = userInfo.get(UserInformationManager.USER_WEIGHT);

        String generalHeader = "% Start Date [YY-MM-DD]: "+startDate+"\n"+
                               "% Start Time [hh-mm-ss]: "+startTime+"\n"+
                               "% \n"+
                               "% Device: "+device+"\n"+
                               "% Android Version : "+androidVersion+"\n"+
                               "% User (sex, age, height [cm], weight [kg]): "+userSex+", "+userAge+", "+userHeight+", "+userWeight+"\n"+
                               "% Smartphone Position: "+smartphonePosition+"\n";

        return generalHeader;
    }


    public String getRelationHeader(int sensorId) {
        String relationHeader = "";
        switch (sensorId) {
            case Sensor.TYPE_ACCELEROMETER:
                relationHeader = "@RELATION \tAcceleration\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tacceleration_x [m/s^2] numeric\n" +
                        "@ATTRIBUTE \tacceleration_y [m/s^2] numeric\n" +
                        "@ATTRIBUTE \tacceleration_z [m/s^2] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                relationHeader = "@RELATION \tAmbient_Temperature\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tambient_temperature [°C] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                relationHeader = "@RELATION \tRotation_Vector\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tx*sin(Ѳ/2) [°] numeric\n" +
                        "@ATTRIBUTE \ty*sin(Ѳ/2) [°] numeric\n" +
                        "@ATTRIBUTE \tz*sin(Ѳ/2) [°] numeric\n" +
                        "@ATTRIBUTE \tcos(Ѳ/2) [°] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                relationHeader = "@RELATION \tGeomagnetic_Rotation_Vector\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tx*sin(Ѳ/2) [°] numeric\n" +
                        "@ATTRIBUTE \ty*sin(Ѳ/2) [°] numeric\n" +
                        "@ATTRIBUTE \tz*sin(Ѳ/2) [°] numeric\n" +
                        "@ATTRIBUTE \tcos(Ѳ/2) [°] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_GRAVITY:
                relationHeader = "@RELATION \tGravity\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tgravity_x [m/s^2] numeric\n" +
                        "@ATTRIBUTE \tgravity_y [m/s^2] numeric\n" +
                        "@ATTRIBUTE \tgravity_z [m/s^2] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_GYROSCOPE:
                relationHeader = "@RELATION \tAngular_Speed\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tangular_speed_x [rad/s] numeric\n" +
                        "@ATTRIBUTE \tangular_speed_y [rad/s] numeric\n" +
                        "@ATTRIBUTE \tangular_speed_z [rad/s] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                relationHeader = "@RELATION \tUncalibrated_Angular_Speed\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tangular_speed_x [rad/s] numeric\n" +
                        "@ATTRIBUTE \tangular_speed_y [rad/s] numeric\n" +
                        "@ATTRIBUTE \tangular_speed_z [rad/s] numeric\n" +
                        "@ATTRIBUTE \testimated_drift_x [rad/s] numeric\n" +
                        "@ATTRIBUTE \testimated_drift_y [rad/s] numeric\n" +
                        "@ATTRIBUTE \testimated_drift_z [rad/s] numeric\n" +
                        "@DATA \n";

                break;
            case Sensor.TYPE_HEART_RATE:
                relationHeader = "@Heart_Rate\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \theart_rate [bpm] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_LIGHT:
                relationHeader = "@RELATION \tLight\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tlight [lx] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                relationHeader = "@RELATION \tLinear_Acceleration\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tacceleration_x [m/s^2] numeric\n" +
                        "@ATTRIBUTE \tacceleration_y [m/s^2] numeric\n" +
                        "@ATTRIBUTE \tacceleration_z [m/s^2] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                relationHeader = "@RELATION \tMagnetic_Field\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tmagnetic_field_x [μT] numeric\n" +
                        "@ATTRIBUTE \tmagnetic_field_y [μT] numeric\n" +
                        "@ATTRIBUTE \tmagnetic_field_z [μT] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                relationHeader = "@RELATION \tUncalibrated_Magnetic_Field\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tuncalibrated_magnetic_field_x [μT] numeric\n" +
                        "@ATTRIBUTE \tuncalibrated_magnetic_field_y [μT] numeric\n" +
                        "@ATTRIBUTE \tuncalibrated_magnetic_field_z [μT] numeric\n" +
                        "@ATTRIBUTE \tbias_x [μT] numeric\n" +
                        "@ATTRIBUTE \tbias_y [μT] numeric\n" +
                        "@ATTRIBUTE \tbias_z [μT] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_PRESSURE:
                relationHeader = "@RELATION \tPressure\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tpressure [mbars] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_PROXIMITY:
                relationHeader = "@RELATION \tDistance\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tdistance [cm] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                relationHeader = "@RELATION \tRelative_Humidity\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \trelative_humidity [%] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                relationHeader = "@RELATION \tGeomagnetic_Rotation_Vector\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tx*sin(Ѳ/2) [°] numeric\n" +
                        "@ATTRIBUTE \ty*sin(Ѳ/2) [°] numeric\n" +
                        "@ATTRIBUTE \tz*sin(Ѳ/2) [°] numeric\n" +
                        "@ATTRIBUTE \tcos(Ѳ/2) [°] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                relationHeader = "@RELATION \tSignificant_Motion\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tsignificant_motion numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_STEP_COUNTER:
                relationHeader = "@RELATION \tStep_Counter\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tsteps [number_of_steps] numeric\n" +
                        "@DATA \n";
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                relationHeader = "@RELATION \tStep_Detector\n" +
                        "@ATTRIBUTE \ttime [s] numeric\n" +
                        "@ATTRIBUTE \tstep_detected numeric\n" +
                        "@DATA \n";
                break;
            default:
                relationHeader = "";
                break;

        }
        return relationHeader;
    }

}
