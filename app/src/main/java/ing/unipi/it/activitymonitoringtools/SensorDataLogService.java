package ing.unipi.it.activitymonitoringtools;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * @brief Class that describes the background service that collects sensor samples and saves them into a file
 */
public class SensorDataLogService extends SensorService implements SensorEventListener {

    public static final String TAG = SensorDataLogService.class.getName();

    private PowerManager.WakeLock mWakeLock = null;

    private int activeSensors = 0;

    private File[] samplesDirectories;
    private File[] samplesFiles;
    private boolean[] isFirstSample;
    private long[] lastUpdateTimestamp;
    private long[] cont;

    private int numSamplesPerSec[];
    private int dimbuf[];

    private SampleDataBuffer[] samplesBuffer;



    private NotificationManager notificationManager;

    private String smartPhonePosition;

    private SavingSamplesTimer timer;
    private boolean timerStarted = false;


    public SensorDataLogService() {
        super();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        actionScreenOffReceiver = new ActionScreenOffReceiver(sensorManager, this);

        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        registerReceiver(actionScreenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.e("Sensor data log service started", "service started");


        timer = new SavingSamplesTimer(5000, 1000);//five seconds

        if (!timerStarted) {
            timer.start();
            timerStarted = true;
        } else {
            timer.cancel();
            timerStarted = false;
        }


        Bundle extras = intent.getExtras();
        smartPhonePosition = (String) extras.get("Smartphone position");
        selectedSensorsData = (List<SensorInfo>) extras.getSerializable("Selected sensors");
        activeSensors = selectedSensorsData.size();

        samplesDirectories = new File[activeSensors];
        samplesFiles = new File[activeSensors];
        isFirstSample = new boolean[activeSensors];
        lastUpdateTimestamp = new long[activeSensors];
        cont = new long[activeSensors];

        numSamplesPerSec = new int[activeSensors];
        dimbuf = new int[activeSensors];

        samplesBuffer = new SampleDataBuffer[activeSensors];

        //todo create a buffer for each sensor
        long now = System.currentTimeMillis();

        String generalHeader = getGeneralHeader(now, smartPhonePosition);
        // Toast.makeText(getApplicationContext(), generalHeader, Toast.LENGTH_LONG).show();

        for (int i = 0; i < selectedSensorsData.size(); i++) {
            isFirstSample[i] = true;
            lastUpdateTimestamp[i] = 0;
            cont[i] = 0;

            String sensorHeader = getSensorSpecificHeader(selectedSensorsData.get(i).getSensorType(), selectedSensorsData.get(i));
            String relationHeader = getRelationHeader(selectedSensorsData.get(i).getSensorType());

            //Creation of a directory and a file for each sensor
            String sensorName = SensorInfo.getSensorNameById(selectedSensorsData.get(i).getSensorType(), selectedSensorsData.get(i).getSensorName());
            samplesDirectories[i] = Utilities.createDirectory("ActivityMonitoringTools/SensorDataLog/Samples/" + sensorName +
                    "/" + Utilities.getDateTimeFromMillis(now, "yy-MM-dd"));
            samplesFiles[i] = Utilities.createFile(samplesDirectories[i], Utilities.getDateTimeFromMillis(now, "kk-mm") + ".arff");

            //Creation of a buffer for each sensor
            numSamplesPerSec[i] = selectedSensorsData.get(i).getNumSamplesPerSec(selectedSensorsData.get(i).getSensorSpeed());
            dimbuf[i] = 10 * numSamplesPerSec[i];//ten seconds of samples
            samplesBuffer[i] = new SampleDataBuffer(dimbuf[i]);


            if (Utilities.getFileSize(samplesFiles[i]) == 0) {

                Utilities.writeData(samplesFiles[i], sensorHeader);
                Utilities.writeData(samplesFiles[i], generalHeader);
                Utilities.writeData(samplesFiles[i], relationHeader);
            }


        }


        registerListeners();

        mWakeLock.acquire();

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(actionScreenOffReceiver);

        unregisterListener();
        timer.cancel();

        mWakeLock.release();

        //notificationManager.cancelAll();
        super.onDestroy();
    }

    public void registerListeners() {

        for (SensorInfo s : selectedSensorsData) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(s.getSensorType()), s.getSensorSpeed());
        }

        Log.e("Listeners registered", "listeners registered");
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        for (int i = 0; i < activeSensors; i++) {
            if (event.sensor.getType() == selectedSensorsData.get(i).getSensorType()) {//to select the right file/buffer ect
                long timestampInMillis = Utilities.getEventTimestampInMillis(event.timestamp);


                if (isFirstSample[i]) {
                    lastUpdateTimestamp[i] = timestampInMillis;
                    isFirstSample[i] = false;
                }

                long diff = timestampInMillis - lastUpdateTimestamp[i];
                cont[i] += diff;
                lastUpdateTimestamp[i] = timestampInMillis;

                String sensedValues = "";

                for (int j = 0; j < event.values.length; j++) {
                    sensedValues += ", " + event.values[j];
                }

                SampleData sample = new SampleData(Utilities.getTimeInSeconds(cont[i]), event.values);
                //Utilities.writeData(samplesFiles[i],sample.toString() );//writing the single sample to the file
                //Log.e(""+timestampInMillis,sample.toString() );

                samplesBuffer[i].putSample(sample);

            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public String getSensorSpecificHeader(int sensorId, SensorInfo sensorInfo) {

        String sensorName = SensorInfo.getSensorNameById(sensorId, sensorInfo.getSensorName());
        float sensorRange = sensorInfo.getMaxRange();
        String sensorSpeed = sensorInfo.getAndroidSamplingRateById(sensorInfo.getSensorSpeed());


        String sensorHeader = "% " + sensorName + " Track\n%\n" +
                "% Range: " + sensorRange + " " + SensorInfo.getSensorUnitById(sensorInfo.getSensorType()) + "\n" +
                "% Android Sampling Rate : " + sensorSpeed + "\n";
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

        String generalHeader = "% Start Date [YY-MM-DD]: " + startDate + "\n" +
                "% Start Time [hh-mm-ss]: " + startTime + "\n" +
                "% \n" +
                "% Device: " + device + "\n" +
                "% Android Version : " + androidVersion + "\n" +
                "% User (sex, age, height [cm], weight [kg]): " + userSex + ", " + userAge + ", " + userHeight + ", " + userWeight + "\n" +
                "% Smartphone Position: " + smartphonePosition + "\n";

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


    class SavingSamplesTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public SavingSamplesTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            Log.e("Time's up", "time's up");

            for (int i = 0; i < activeSensors; i++) {
                new SavingSamplesTask(i, samplesBuffer[i], samplesFiles[i]).execute();
            }

            this.start();

        }
    }

    class SavingSamplesTask extends AsyncTask<String, String, String> {

        int id;
        SampleDataBuffer buffer;
        File outFile;

        public SavingSamplesTask(int id, SampleDataBuffer buf, File file) {
            this.id = id;
            this.buffer = buf;
            this.outFile = file;
        }


        @Override
        protected String doInBackground(String... params) {


            SampleData[] samples = buffer.getNSamples(buffer.getDimBuffer()/2);
           // Utilities.writeData(outFile,"-----------\n");
            for (SampleData s : samples) {
                Utilities.writeData(outFile, s.toString());
            }

           /* SampleData s = buffer.getSample();//prendo dal buffer un campione alla volta e lo scrivo su file, ci vuole un timer che scatta ogni ms
            Utilities.writeData(outFile,s.toString());*/


            return null;
        }
    }


}
