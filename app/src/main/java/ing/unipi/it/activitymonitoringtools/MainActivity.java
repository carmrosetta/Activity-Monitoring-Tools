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
    //todo decidere come trattare queste info che per ora sono costanti ma successivamente non lo saranno ma devono essere scelte dall'utente
    public final static String SMARTPHONE_POSITION  = "Left front trouser pocket";
    public final static int SAMPLING_FREQUENCY = SensorManager.SENSOR_DELAY_FASTEST;


    ListView mListView;

    Button btnShowCheckedItems;


    ArrayList<Tool> mTools;

    MyCustomAdapter<Tool> mAdapter;


    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        bindComponents();

        init();

        addListeners();

    }


    private void bindComponents() {

// TODO Auto-generated method stub

        mListView = (ListView) findViewById(android.R.id.list);

        btnShowCheckedItems = (Button) findViewById(R.id.btnShowCheckedItems);

    }



    private void init() {

// TODO Auto-generated method stub

        mTools = new ArrayList<Tool>();

        mTools.add(new Tool("Sensor Data Logger"));

        mTools.add(new Tool("Gait Recognition"));

        mTools.add(new Tool("Posture Detection"));

        mTools.add(new Tool("Impact Detection"));


        mAdapter = new MyCustomAdapter<Tool>(this, mTools);

        mListView.setAdapter(mAdapter);

    }



    private void addListeners() {

// TODO Auto-generated method stub

        btnShowCheckedItems.setOnClickListener(this);

    }


    @Override

    public void onClick(View v) {

// TODO Auto-generated method stub


        if(mAdapter != null) {

            ArrayList<Tool> mArrayTools = mAdapter.getCheckedItems();

            Log.d(MainActivity.class.getSimpleName(), "Selected Items: " + mArrayTools.toString());

            Toast.makeText(getApplicationContext(), "Selected Items: " + mArrayTools.toString(), Toast.LENGTH_LONG).show();


            //todo fare partire il servizio che si occupa di inserire i campioni dell'accelerometro in un buffer circolare

           /* Intent intent = new Intent(getApplicationContext(), AccelerationSamplingService.class);
            startService(intent);*/
            finish();


        }

    }


}
