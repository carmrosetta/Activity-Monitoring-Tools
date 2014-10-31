package ing.unipi.it.activitymonitoringtools;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;

import java.util.List;

/**
 * @brief Class that represents all the background services that use sensors and that have to cope
 * with the ACTION_SCREEN_OFF event
 */
public class SensorService extends Service {

    public List<SensorInfo> selectedSensorsData;
    public SensorManager sensorManager;
    public ActionScreenOffReceiver actionScreenOffReceiver;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
