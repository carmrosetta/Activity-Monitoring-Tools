package ing.unipi.it.activitymonitoringtools;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utilities {

    /**
     * @brief Method that shows a notification as a persistent icon in the status bar
     * @param context the application context
     * @param notificationManager object that notifies the user that an event happened
     * @param message a String representing the message for the user
     * @param cls class to which belongs the activity that the user can start by clicking on the notification
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void showNotification(Context context, NotificationManager notificationManager, String message, Class cls) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Sensor Data Logger");
        builder.setContentText("The sensor data logger service is active");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker(message);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(false);

        builder.setContentTitle(message);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, cls), PendingIntent.FLAG_UPDATE_CURRENT);//studiare i flag
        builder.setContentIntent(contentIntent);
        notificationManager.notify(0, builder.build());

    }

    /**
     * @brief Method that shows an alert dialog
     * @param context the application context
     * @param message a String representing the message for the user
     */
    public static void showAlertDialog(Context context, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertBuilder.show();
    }

    public static String getSensorNameById(int sensorId, String name) {
        String sensorName = "";
        switch (sensorId) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorName = "Accelerometer";
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sensorName = "Thermometer";
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                sensorName = "Game Rotation Sensor";
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                sensorName = "Geomagnetic Rotation Vector Sensor";
                break;
            case Sensor.TYPE_GRAVITY:
                sensorName = "Gravity Sensor";
                break;
            case Sensor.TYPE_GYROSCOPE:
                sensorName = "Gyroscope";
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                sensorName = "Uncalibrated Gyroscope";
                break;
            case Sensor.TYPE_HEART_RATE:
                sensorName = "Heart Rate Sensor";
                break;
            case Sensor.TYPE_LIGHT:
                sensorName = "Light Sensor";
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                sensorName = "Linear Acceleration Sensor";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorName = "Magnetic Field Sensor";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                sensorName = "Uncalibrated Magnetic Field Sensor";
                break;
            case Sensor.TYPE_PRESSURE:
                sensorName = "Barometer";
                break;
            case Sensor.TYPE_PROXIMITY:
                sensorName = "Proximity Sensor";
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                sensorName = "Relative Humidity Sensor";
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                sensorName = "Rotation Vector Sensor";
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                sensorName = "Significant Motion Sensor";
                break;
            case Sensor.TYPE_STEP_COUNTER:
                sensorName = "Step Counter";
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                sensorName = "Step Detector";
                break;
            default:
                sensorName = name;
                break;

        }
        return sensorName;
    }

    public static String getSensorUnitById(int sensorId) {
        String unit = "";
        switch (sensorId) {
            case Sensor.TYPE_ACCELEROMETER:
                unit = "[m/s^2]";
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                unit = "°C";
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                unit = "°";
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                unit = "°";
                break;
            case Sensor.TYPE_GRAVITY:
                unit = "[m/s^2]";
                break;
            case Sensor.TYPE_GYROSCOPE:
                unit = "[rad/sec]";
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                unit = "[rad/sec]";
                break;
            case Sensor.TYPE_HEART_RATE:
                unit = "[bpm]";
                break;
            case Sensor.TYPE_LIGHT:
                unit = "[lx]";
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                unit = "[m/s^2]";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                unit = "[μT]";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                unit = "[μT]";
                break;
            case Sensor.TYPE_PRESSURE:
                unit = "[mbars]";
                break;
            case Sensor.TYPE_PROXIMITY:
                unit = "[cm]";
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                unit = "[%]";
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                unit = "°";
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                unit = "";
                break;
            case Sensor.TYPE_STEP_COUNTER:
                unit = "[number of steps]";
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                unit = "";
                break;
            default:
                unit = "";
                break;

        }
        return unit;
    }

    public static String createRelationHeader(int sensorId) {
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

    public static void writeData(File file, String data) {

        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
            fw.close();
        } catch (IOException e) {
        }
    }

    public static File createDirectory(String dirName) {

        File directory = null;

        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getAbsolutePath();

        directory = new File(path, dirName);

        if (!directory.exists()) {
            directory.mkdirs();
        }


        return directory;

    }

    public static File createFile(File directory, String fileName) {
        File file = null;

        try {
            file = new File(directory, fileName);
            file.createNewFile();
        } catch (IOException e) {
            Log.e("ERROR", "Exception while creating file:" + e.toString());
        }

        return file;
    }

    public static String getTimeInSeconds(long timeInMillis) {

        int seconds = (int) (timeInMillis / 1000);
        int milliseconds = (int) (timeInMillis % 1000);


        String timeInSeconds = "" + String.format(seconds + "." + String.format("%03d", milliseconds));
        return timeInSeconds;
    }

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getDeviceName() {
        String deviceName = "";
        String manufacturer = capitalize(Build.MANUFACTURER);
        String model = Build.MODEL;

        if (model.startsWith(manufacturer))
            deviceName = model;
        else deviceName = manufacturer + " " + model;

        return deviceName;
    }

    public static String getAndroidVersion() {
        StringBuilder builder = new StringBuilder();
        builder.append("Android ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append("  (").append(fieldName).append(") , ");
                builder.append("sdk = ").append(fieldValue);
            }
        }
        return builder.toString();

    }

    public static String getDateTimeFromMillis(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static long getFileSize(File file) {
        return file.length() / 1024;
    }

    public static String getAndroidSamplingRateById(int sensorSamplingRate) {
        String androidSamplingRate = "";

        switch (sensorSamplingRate) {
            case SensorManager.SENSOR_DELAY_NORMAL:
                androidSamplingRate = "SensorManager.SENSOR_DELAY_NORMAL";
                break;
            case SensorManager.SENSOR_DELAY_UI:
                androidSamplingRate = "SensorManager.SENSOR_DELAY_NORMAL";
                break;
            case SensorManager.SENSOR_DELAY_GAME:
                androidSamplingRate = "SensorManager.SENSOR_DELAY_GAME";
                break;
            case SensorManager.SENSOR_DELAY_FASTEST:
                androidSamplingRate = "SensorManager.SENSOR_DELAY_FASTEST";
                break;
        }

        return androidSamplingRate;
    }


}

