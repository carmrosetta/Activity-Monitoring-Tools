package ing.unipi.it.activitymonitoringtools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import ing.unipi.it.activitymonitoringtools.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * @brief This class allows to select the position of the smartphone and to choose which sensors to use and at which speed
 */
public class SensorDataLogSettings extends Activity {

    public final static int SENSORS_SELECTED = 1;

    private Spinner smartPhonePositionSpinner;

    private String smartPhonePosition = null;

    private String[] arraySmartPhonePosition;

    private LinearLayout sensorActivationList;

    private SensorManager sensorManager;

    private List<Sensor> sensorList;

    private CheckBox[] sensors;

    private Spinner[] sensorSamplingSpeeds;

    private LinearLayout[] sensorChoices;

    private int[] sensorTypes;

    private ArrayAdapter<CharSequence> [] samplingSpeedArrayAdapter;

    private int[] sensorDelays;

    private int selectedSensors = 0;

    private List<SensorInfo> selectedSensorsList;

    private Button btnSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_selection);
        setTitle(R.string.app_name);

        initSmartPhonePositionSpinner();

        initSensorActivationList();

        btnSaveData = (Button)findViewById(R.id.btnSaveData);
        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonPressed();

            }
        });
    }

    /**
     * @brief Method that runs when the user press the button
     * It checks if the user has chosen at least a sensor. If it hasn't chosen any sensor, it shows
     * a dialog which inform the user that he/she has to use at least a sensor.
     * Otherwise, it returns the sensor position and the list of the chosen sensors to the main activity
     */
    public void onSaveButtonPressed() {
        if(selectedSensors == 0) {
            //Toast.makeText(getApplicationContext(), "You must select at least a sensor", Toast.LENGTH_SHORT).show();
           Utilities.showAlertDialog(this, "You must select at least a sensor!");

        }
        else {
            //Toast.makeText(getApplicationContext(), "You have chosen "+selectedSensors+" sensors", Toast.LENGTH_SHORT).show();
            sendData();
        }
    }

    /**
     * @brief Method that sends information about the smartphone position and the selected sensors
     * to the main activity
     */
    public void sendData() {

        selectedSensorsList = new LinkedList<SensorInfo>();

        for(int k = 0; k < sensorList.size(); k++) {
            if(sensors[k].isChecked()) {
                SensorInfo sensorInfo = new SensorInfo(sensorList.get(k).getType(), sensorList.get(k).getName(), sensorDelays[k], sensorList.get(k).getMaximumRange());
                selectedSensorsList.add(sensorInfo);
            }
        }

        Intent data = new Intent();
        data.putExtra("Smartphone position", smartPhonePosition);
        data.putExtra("Selected sensors", (java.io.Serializable)selectedSensorsList);

        setResult(SENSORS_SELECTED, data);
        finish();

    }




    /**
     * @brief This method initializes the list from which the user can choose which sensors he/she
     * wants to activate
     */
    public void initSensorActivationList() {

        sensorActivationList = (LinearLayout) findViewById(R.id.sensorListLinearLayout);
        TextView tvSensorList = new TextView(this);
        tvSensorList.setText("Please select which sensors you want to use and at which speed ");
        tvSensorList.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        sensorActivationList.addView(tvSensorList);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        sensors = new CheckBox[sensorList.size()];

        sensorSamplingSpeeds = new Spinner[sensorList.size()];

        sensorChoices = new LinearLayout[sensorList.size()];

        sensorTypes = new int[sensorList.size()];

        samplingSpeedArrayAdapter = new ArrayAdapter[sensorList.size()];

        sensorDelays = new int[sensorList.size()];

        for(int i = 0; i < sensorList.size(); i++) {

            final int j = i;

            samplingSpeedArrayAdapter[i] = ArrayAdapter.createFromResource(this, R.array.delay_rates_array, android.R.layout.simple_spinner_item);
            samplingSpeedArrayAdapter[i].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sensorChoices[i] = new LinearLayout(this);
            sensorChoices[i].setOrientation(LinearLayout.HORIZONTAL);
            sensorChoices[i].setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

            sensors[i] = new CheckBox(this);
            sensorSamplingSpeeds[i] = new Spinner(this);

            sensorChoices[i].addView(sensors[i]);
            sensors[i].setText(Utilities.getSensorNameById(sensorList.get(i).getType(), sensorList.get(i).getName()));
            sensorTypes[i] = sensorList.get(i).getType();
            sensors[i].setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            sensors[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        selectedSensors += 1;
                    }
                    else {
                        selectedSensors -= 1;
                    }
                }
            });
            sensorChoices[i].addView(sensorSamplingSpeeds[i]);
            sensorSamplingSpeeds[i].setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            sensorSamplingSpeeds[i].setAdapter(samplingSpeedArrayAdapter[i]);
            sensorSamplingSpeeds[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            sensorDelays[j] = SensorManager.SENSOR_DELAY_NORMAL;
                            break;
                        case 1:
                            sensorDelays[j] = SensorManager.SENSOR_DELAY_UI;
                            break;
                        case 2:
                            sensorDelays[j] = SensorManager.SENSOR_DELAY_GAME;
                            break;
                        case 3:
                            sensorDelays[j] = SensorManager.SENSOR_DELAY_FASTEST;

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            sensorActivationList.addView(sensorChoices[i]);
        }

    }

    /**
     * @brief Method that initializes the smartphone position spinner
     */
    public void initSmartPhonePositionSpinner() {

        ArrayAdapter<CharSequence> adapter = null;

        arraySmartPhonePosition = getResources().getStringArray(R.array.smart_phone_position_array);

        smartPhonePositionSpinner = (Spinner) findViewById(R.id.smart_phone_position);

        adapter = ArrayAdapter.createFromResource(this, R.array.smart_phone_position_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        smartPhonePositionSpinner.setAdapter(adapter);
        smartPhonePositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smartPhonePosition = arraySmartPhonePosition[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
