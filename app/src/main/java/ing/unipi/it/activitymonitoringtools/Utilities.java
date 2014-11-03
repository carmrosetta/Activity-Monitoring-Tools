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
import java.util.Date;

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

    public static long getEventTimestampInMillis(long eventTimestamp) {
        return (new Date()).getTime() + (eventTimestamp - System.nanoTime()) / 1000000L;

    }

}

