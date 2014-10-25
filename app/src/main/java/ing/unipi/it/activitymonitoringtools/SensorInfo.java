package ing.unipi.it.activitymonitoringtools;

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
}
