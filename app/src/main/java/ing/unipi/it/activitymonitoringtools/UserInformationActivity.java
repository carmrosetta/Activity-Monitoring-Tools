package ing.unipi.it.activitymonitoringtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class UserInformationActivity extends Activity {

    UserInformationManager userInformationManager;

    TextView header;
    Spinner userSex;
    EditText userAge;
    EditText userHeight;
    EditText userWeight;
    Button saveUserInfoBtn;
    Button clearUserInfoButton;

    String sex;
    String age;
    String height;
    String weight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        userInformationManager = new UserInformationManager(getApplicationContext());

        initGui();


        if(userInformationManager.userInformationSaved()) {
            //Toast.makeText(getApplicationContext(),"user logged in "+ userInformationManager.userInformationSaved(), Toast.LENGTH_LONG).show();
            header.setText("Update or clear your information");
            HashMap<String, String> user = userInformationManager.getUserInformation();

            userAge.setText(user.get(UserInformationManager.USER_AGE));
            userHeight.setText(user.get(UserInformationManager.USER_HEIGHT));
            userWeight.setText(user.get(UserInformationManager.USER_WEIGHT));

            saveUserInfoBtn.setText("Update your data");
            clearUserInfoButton.setVisibility(View.VISIBLE);
            clearUserInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userInformationManager.clearUserInformation();
                }
            });


        }
        else {
            //Toast.makeText(getApplicationContext(),"user logged in "+ userInformationManager.userInformationSaved(), Toast.LENGTH_LONG).show();
            
            saveUserInfoBtn.setVisibility(View.VISIBLE);

        }

        saveUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });



    }

    public void initGui() {

        header = (TextView) findViewById(R.id.header_startup_activity);
        userSex = (Spinner) findViewById(R.id.user_sex);
        userAge = (EditText) findViewById(R.id.user_age);
        userHeight = (EditText) findViewById(R.id.user_height);
        userWeight = (EditText) findViewById(R.id.user_weight);
        saveUserInfoBtn = (Button) findViewById(R.id.save_user_info_btn);
        clearUserInfoButton = (Button) findViewById(R.id.clear_user_info_btn);

    }

    /**
     * Function that checks if user's information have been provided
     * @return true if all fields are provided, false otherwise
     */

    public boolean userDataInserted() {

        String[] genderValues = getResources().getStringArray(R.array.gender_values);
        sex = genderValues[userSex.getSelectedItemPosition()];


        age = userAge.getText().toString();
        height = userHeight.getText().toString();
        weight = userWeight.getText().toString();

        if(age.equals("")){
            userAge.setError(getString(R.string.user_age_required));
            userAge.requestFocus();
            return false;
        }

        if(height.equals("")){
            userHeight.setError(getString(R.string.user_height_required));
            userHeight.requestFocus();
            return false;
        }


       if(weight.equals("")) {
            userWeight.setError(getString(R.string.user_weight_required));
            userWeight.requestFocus();
            return false;
        }

        return true;



    }

    /**
     * save user preferences
     */
    public void saveUserData() {

        if(userDataInserted()) {
            userInformationManager.saveUserInformation(sex, age, height, weight);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_preferences, menu);
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
    }
}
