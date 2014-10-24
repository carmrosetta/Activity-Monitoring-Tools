package ing.unipi.it.activitymonitoringtools;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by carmen on 24/10/14.
 */
public class SensorDataLog extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
