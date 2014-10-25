package ing.unipi.it.activitymonitoringtools;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import ing.unipi.it.activitymonitoringtools.R;

public class SensorDataLogSettings extends Activity {

    private Spinner smartPhonePositionSpinner;

    private String smartPhonePosition = null;
    private String[] arraySmartPhonePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_selection);
        setTitle(R.string.app_name);
        initSmartPhonePositionSpinner();
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


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sensor_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
