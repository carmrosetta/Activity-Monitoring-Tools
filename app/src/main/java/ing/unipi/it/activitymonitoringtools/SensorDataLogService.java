package ing.unipi.it.activitymonitoringtools;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SensorDataLogService extends Service {

    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;


    public SensorDataLogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
