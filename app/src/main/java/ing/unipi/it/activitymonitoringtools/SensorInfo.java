package ing.unipi.it.activitymonitoringtools;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.io.Serializable;

/**
 * @brief This class represents the most relevant information about a sensor
 */
public class SensorInfo implements Serializable {
    private int sensorType;
    private String sensorName;
    private int sensorSpeed;
    private float maxRange;

    /**
     * @brief Constructor
     * @param sensorType Integer which specifies the sensor type
     * @param sensorName String that represents the sensor name
     * @param sensorSpeed Integer that indicates the sensor sampling speed
     * @param maxRange Float that represents the maximum range of the sensor in the sensor's unit
     */
    public SensorInfo(int sensorType, String sensorName, int sensorSpeed, float maxRange) {
        this.sensorType = sensorType;
        this.sensorName = sensorName;
        this.sensorSpeed = sensorSpeed;
        this.maxRange = maxRange;
    }

    /**
     * @brief This method allows to get the sensor type
     * @return an Integer which specifies the sensor type
     */
    public int getSensorType() {
        return sensorType;
    }

    /**
     * @brief This method sets the type of the sensor
     * @param sensorType Integer which specifies the sensor type
     */
    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }

    /**
     * @brief This method retrieves the name of the sensor
     * @return a String that represents the name of the sensor
     */
    public String getSensorName() {
        return sensorName;
    }

    /**
     * @brief This method sets the name of the sensor
     * @param sensorName a String which represents the name of the sensor
     */
    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    /**
     * @brief This method allows to get the rate which sensor events should be delivered at.
     * This is only a hint to the system, indeed events may be received faster or slower than the
     * specified rate.
     *
     * @return an Integer that indicates the sensor sampling rate
     * */
    public int getSensorSpeed() {
        return sensorSpeed;
    }

    /**
     * @brief This method allows to specify the rate which  the sensor events should be delivered at
     * @param sensorSpeed an Integer which specifies one of the four Android sensor delays
     */
    public void setSensorSpeed(int sensorSpeed) {
        this.sensorSpeed = sensorSpeed;
    }

    /**
     * @brief This method gets maximum range of the sensor in the sensor's unit
     * @return a Float indicating the sensor maximum range
     */
    public float getMaxRange() {
        return maxRange;
    }

    /**
     * @brief This method allows to set the sensor maximum range
     * @param maxRange a Float indicating the sensor maximum range
     */
    public void setMaxRange(float maxRange) {
        this.maxRange = maxRange;
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

    public String getAndroidSamplingRateById(int sensorSamplingRate) {
        String androidSamplingRate = "";

        switch (sensorSamplingRate) {
            case SensorManager.SENSOR_DELAY_NORMAL:
                androidSamplingRate = "SensorManager.SENSOR_DELAY_NORMAL";
                break;
            case SensorManager.SENSOR_DELAY_UI:
                androidSamplingRate = "SensorManager.SENSOR_DELAY_UI";
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



    public int getNumSamplesPerSec(int androidSamplingRate) {
        int numSamplesPerSec = 0;

        switch (androidSamplingRate) {
            case SensorManager.SENSOR_DELAY_NORMAL:
                numSamplesPerSec = 5;
                break;
            case SensorManager.SENSOR_DELAY_UI:
                numSamplesPerSec = 15;
                break;
            case SensorManager.SENSOR_DELAY_GAME:
                numSamplesPerSec = 50;
                break;
            case SensorManager.SENSOR_DELAY_FASTEST:
                numSamplesPerSec = 100;
                break;
        }

        return numSamplesPerSec;
    }



}
