package ing.unipi.it.activitymonitoringtools;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;

import java.util.List;

/**
 * Created by carmen on 29/10/14.
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
