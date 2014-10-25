package ing.unipi.it.activitymonitoringtools;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @file MainActivity.java
 * @brief Activity through which the user chooses which modules to activate
 */

public class MainActivity extends Activity implements View.OnClickListener {

    ListView mListView;

    Button btnShowCheckedItems;


    ArrayList<Tool> mTools;

    MyCustomAdapter<Tool> mAdapter;

    UserInformationManager userInformationManager;


    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        userInformationManager = new UserInformationManager(getApplicationContext());
        userInformationManager.checkUserInformationSaved();

        bindComponents();
        init();
        addListeners();

    }


    private void bindComponents() {
        mListView = (ListView) findViewById(android.R.id.list);
        btnShowCheckedItems = (Button) findViewById(R.id.btnShowCheckedItems);
    }



    private void init() {
        mTools = new ArrayList<Tool>();
        mTools.add(new Tool("Sensor Data Logger"));
        mTools.add(new Tool("Gait Recognition"));
        mTools.add(new Tool("Posture Detection"));
        mTools.add(new Tool("Impact Detection"));

        mAdapter = new MyCustomAdapter<Tool>(this, mTools);
        mListView.setAdapter(mAdapter);
    }



    private void addListeners() {
       btnShowCheckedItems.setOnClickListener(this);
    }


    @Override

    public void onClick(View v) {
        if(mAdapter != null) {
            ArrayList<Tool> mArrayTools = mAdapter.getCheckedItems();
            Log.d(MainActivity.class.getSimpleName(), "Selected Items: " + mArrayTools.toString());
            Toast.makeText(getApplicationContext(), "Selected Items: " + mArrayTools.toString(), Toast.LENGTH_LONG).show();

           /* Intent intent = new Intent(getApplicationContext(), AccelerationSamplingService.class);
            startService(intent);
            finish();*/
       }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), UserInformationActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
