package ing.unipi.it.activitymonitoringtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @file MainActivity.java
 * @brief Activity through which the user chooses which modules of the application he/she wants to activate
 */

public class MainActivity extends Activity implements View.OnClickListener {

    public final static int SELECT_SENSORS = 1;

    private ListView mListView;

    private Button btnShowCheckedItems;


    private ArrayList<Tool> mTools;

    private MyCustomAdapter<Tool> mAdapter;

    private UserInformationManager userInformationManager;

    private String smartPhonePosition;

    private List<SensorInfo> selectedSensorsList;


    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        userInformationManager = new UserInformationManager(getApplicationContext());
        userInformationManager.checkUserInformationSaved();

        bindComponents();
        initToolList();
        addOnClickListener();





    }


    /**
     * @brief This method allows to take control of the components of the graphical interface
     */
    private void bindComponents() {
        mListView = (ListView) findViewById(android.R.id.list);
        btnShowCheckedItems = (Button) findViewById(R.id.btnShowCheckedItems);
    }


    /**
     * @brief This method allows to initialize the list of the tools
     */
    private void initToolList() {
        mTools = new ArrayList<Tool>();
        mTools.add(new Tool("Sensor Data Logger"));
        mTools.add(new Tool("Gait Recognition"));
        mTools.add(new Tool("Posture Detection"));
        mTools.add(new Tool("Impact Detection"));

        mAdapter = new MyCustomAdapter<Tool>(this, mTools);

        mListView.setAdapter(mAdapter);

    }


    /**
     * @brief this method sets a listener to the button, which allows to manage the user's choices
     * through the method onClick()
     */
    private void addOnClickListener() {
       btnShowCheckedItems.setOnClickListener(this);
    }


    @Override

    public void onClick(View v) {
        if(mAdapter != null) {
            ArrayList<Tool> mArrayTools = mAdapter.getCheckedItems();
           // Log.d(MainActivity.class.getSimpleName(), "Selected Items: " + mArrayTools.toString());
            Toast.makeText(getApplicationContext(), "Selected Items: " + mArrayTools.toString(), Toast.LENGTH_LONG).show();

            //TODO in base ai pulsanti abilitati devo avviare i servizi giusti con i parametri giusti

           /* Intent intent = new Intent(getApplicationContext(), AccelerationSamplingService.class);
            startService(intent);
            finish();*/
       }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), UserInformationActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_SENSORS) {
            if(resultCode == SensorDataLogSettings.SENSORS_SELECTED) {
                 smartPhonePosition= data.getStringExtra("Smartphone position");
                //Toast.makeText(getApplicationContext(), smartPhonePosition, Toast.LENGTH_SHORT).show();
                selectedSensorsList = (List<SensorInfo>) data.getSerializableExtra("Selected sensors");
            }

        }
    }
}
