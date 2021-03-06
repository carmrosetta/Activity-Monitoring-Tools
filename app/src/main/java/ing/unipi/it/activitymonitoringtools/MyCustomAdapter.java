package ing.unipi.it.activitymonitoringtools;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * @brief Class that extends the Android BaseAdapter class and displays data in a ListView
 * We use this class to display the tools the application offers to the user in the main activity of the application
 * @param <T> Class to which belong the objects displayed in the ListView
 */

public class MyCustomAdapter<T> extends BaseAdapter {
    Context mContext; //the application context
    LayoutInflater mInflater; //object that is used to set the layout of the View
    ArrayList<T> mList; //list of the items shown in the user interface
    SparseBooleanArray mSparseBooleanArray; //object that is used to keep track of the selected items of the list

    ToggleButton[] toggleButtons;


    /**
     * @brief Constructor
     * @param context the application context
     * @param list List containing the objects of which we want to display data and interact with
     */
    public MyCustomAdapter(Context context, ArrayList<T> list) {

        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mSparseBooleanArray = new SparseBooleanArray();
        mList = new ArrayList<T>();
        this.mList = list;
        toggleButtons = new ToggleButton[list.size()];
    }

    /**
     * @brief Method that gets the selected items of the list
     * @return ArrayList containing the selected objects of the list
     */
    public ArrayList<T> getCheckedItems() {
        ArrayList<T> mTempArry = new ArrayList<T>();
        for(int i=0;i<mList.size();i++) {
            if(mSparseBooleanArray.get(i)) {
                mTempArry.add(mList.get(i));
            }
        }
        return mTempArry;
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_with_toggle, null);
        }

        ImageView icon = (ImageView)convertView.findViewById(R.id.toolIcon);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvName);
        tvTitle.setText(mList.get(position).toString());

        ImageButton settingsButton = (ImageButton)convertView.findViewById(R.id.settingsButton);

       // ToggleButton mCheckBox = (ToggleButton) convertView.findViewById(R.id.toggleEnable);
       // mCheckBox.setTag(position);
        //mCheckBox.setChecked(mSparseBooleanArray.get(position));
        //mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);

        toggleButtons[position] = (ToggleButton)convertView.findViewById(R.id.toggleEnable);
        toggleButtons[position].setTag(position);
        toggleButtons[position].setChecked(mSparseBooleanArray.get(position));


        switch (position){

            case 0:
                int iconResId = R.drawable.ic_action_paste;
                icon.setImageResource(iconResId);
                settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, SensorDataLogSettings.class);
                        ((Activity)mContext).startActivityForResult(i,MainActivity.SELECT_SENSORS);

                    }
                });

                break;
            case 1:
                iconResId = R.drawable.ic_action_search;
                icon.setImageResource(iconResId);
                //toggleButtons[1].setChecked(mSparseBooleanArray.get(1));

                toggleButtons[1].setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        toggleButtons[1].setChecked(isChecked);
                        mSparseBooleanArray.put(1, isChecked);}


                });

                break;
            case 2:
                iconResId = R.drawable.ic_action_screen_rotation;
                icon.setImageResource(iconResId);
                //toggleButtons[2].setChecked(mSparseBooleanArray.get(2));
                /*toggleButtons[2].setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked) {
                            if(!toggleButtons[1].isChecked()) {
                               toggleButtons[1].setChecked(true);
                                mSparseBooleanArray.put(1, true);//attivo la gait recognition
                                mSparseBooleanArray.put(2, true);//attivo la posture detection


                            } else {
                                toggleButtons[1].setChecked(true);
                                mSparseBooleanArray.put(2, true);
                            }
                        } else {
                            //toggleButtons[2].setChecked(false);
                            //mSparseBooleanArray.put(2, false);
                        }

                    }
                });*/
                break;
            case 3:
                iconResId = R.drawable.ic_action_warning;
                icon.setImageResource(iconResId);
                break;

        }

        return convertView;
    }


   /* OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);//mette il valore true o false nell'array
            buttonView.setChecked(isChecked);//fa vedere graficamente che il bottone è selezionato o no
        }
    };*/
}


