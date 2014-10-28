package ing.unipi.it.activitymonitoringtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;


public class Tools_Activity extends Activity {

    public final static int SELECT_SENSORS = 1;

    private final static int NUM_TOOLS = 4;

    private final static int SENSOR_LOG = 0;
    private final static int GAIT_REC = 1;
    private final static int POSTURE_DET = 2;
    private final static int IMPACT_DET = 3;

    boolean[] active;

    private String smartPhonePosition;
    private List<SensorInfo> selectedSensorsList;
    private UserInformationManager userInformationManager;
    private ToggleButton enableSensorLog;
    private ToggleButton enableGaitRecog;
    private ToggleButton enablePostureDetect;
    private ToggleButton enableImpactDetect;
    private ImageButton settingsSensorLog;
    private ImageButton settingsGaitRecog;
    private ImageButton settingsPostureDetect;
    private ImageButton settingsImpactDetect;
    private Button btnStartServices;

    private void bindComponents() {

        active = new boolean[NUM_TOOLS];
        for (int i = 0; i < NUM_TOOLS; i++)
            active[i] = false;

        enableSensorLog = (ToggleButton) findViewById(R.id.toggleEnableSensorLog);
        enableGaitRecog = (ToggleButton) findViewById(R.id.toggleEnableGaitRec);
        enablePostureDetect = (ToggleButton) findViewById(R.id.toggleEnablePostureDetect);
        enableImpactDetect = (ToggleButton) findViewById(R.id.toggleEnableImpactDetect);

        settingsSensorLog = (ImageButton) findViewById(R.id.settingsButtonSensorLog);
        settingsGaitRecog = (ImageButton) findViewById(R.id.settingsButtonGaitRec);
        settingsPostureDetect = (ImageButton) findViewById(R.id.settingsButtonPostureDetect);
        settingsImpactDetect = (ImageButton) findViewById(R.id.settingsButtonImpactDetect);

        btnStartServices = (Button) findViewById(R.id.btnStartServices);
    }

    public void setListeners() {

        enableSensorLog.setOnCheckedChangeListener(sensorLogChangeListener);
        settingsSensorLog.setOnClickListener(settingsSensorLogListener);

        enableGaitRecog.setOnCheckedChangeListener(gaitRecChangeListener);

        enablePostureDetect.setOnCheckedChangeListener(postureDetectChangeListener);

        enableImpactDetect.setOnCheckedChangeListener(impactDetectChangeListener);

        btnStartServices.setOnClickListener(startBtnListener);
    }

    CompoundButton.OnCheckedChangeListener sensorLogChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            active[SENSOR_LOG] = isChecked;
        }
    };
    CompoundButton.OnCheckedChangeListener gaitRecChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            active[GAIT_REC] = isChecked;
        }
    };
    CompoundButton.OnCheckedChangeListener postureDetectChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            active[POSTURE_DET] = isChecked;

            if (isChecked && !active[GAIT_REC]) {
                enableGaitRecog.setChecked(true);
            }
        }
    };
    CompoundButton.OnCheckedChangeListener impactDetectChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            active[IMPACT_DET] = isChecked;
        }
    };
    View.OnClickListener settingsSensorLogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), SensorDataLogSettings.class);
            startActivityForResult(i, MainActivity.SELECT_SENSORS);
        }
    };



    View.OnClickListener startBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int activeTools = 0;
            for (int k = 0; k < NUM_TOOLS; k++) {
                if (active[k]) {
                    activeTools++;
                }
            }

            if (activeTools == 0) {
                Toast.makeText(getApplicationContext(), "You didn't choose any service ", Toast.LENGTH_SHORT).show();
            } else {
                startServices();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        userInformationManager = new UserInformationManager(getApplicationContext());
        userInformationManager.checkUserInformationSaved();


        bindComponents();
        setListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tools_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), UserInformationActivity.class);
            startActivity(intent);
            // finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_SENSORS) {
            if (resultCode == SensorDataLogSettings.SENSORS_SELECTED) {
                smartPhonePosition = data.getStringExtra("Smartphone position");
                //Toast.makeText(getApplicationContext(), smartPhonePosition, Toast.LENGTH_SHORT).show();
                selectedSensorsList = (List<SensorInfo>) data.getSerializableExtra("Selected sensors");
            }

        }
    }

    public void startServices() {
        if(enableSensorLog.isChecked()) {
            Toast.makeText(getApplicationContext(), "SENSOR LOG ", Toast.LENGTH_SHORT).show();
        }

        if(enableGaitRecog.isChecked()) {
            Toast.makeText(getApplicationContext(), "GAIT ", Toast.LENGTH_SHORT).show();
        }

        if(enablePostureDetect.isChecked()) {
            Toast.makeText(getApplicationContext(), "POSTURE ", Toast.LENGTH_SHORT).show();
        }

        if(enableImpactDetect.isChecked()) {
            Toast.makeText(getApplicationContext(), "IMPACT ", Toast.LENGTH_SHORT).show();
        }



    }
}
