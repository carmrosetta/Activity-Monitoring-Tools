package ing.unipi.it.activitymonitoringtools;


import java.util.ArrayList;
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
 * @param <T> Class to which belong the objects displayed in the ListView
 */

public class MyCustomAdapter<T> extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<T> mList;
    SparseBooleanArray mSparseBooleanArray;


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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_with_toggle, null);
        }
        ImageView icon = (ImageView)convertView.findViewById(R.id.toolIcon);


        switch (position){

            case 0:
                int iconResId = R.drawable.ic_action_paste;
                icon.setImageResource(iconResId);
                break;
            case 1:
                iconResId = R.drawable.ic_action_search;
                icon.setImageResource(iconResId);
                break;
            case 2:
                iconResId = R.drawable.ic_action_screen_rotation;
                icon.setImageResource(iconResId);
                break;
            case 3:
                iconResId = R.drawable.ic_action_warning;
                icon.setImageResource(iconResId);
                break;

        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvName);
        tvTitle.setText(mList.get(position).toString());
        ToggleButton mCheckBox = (ToggleButton) convertView.findViewById(R.id.toggleEnable);
        mCheckBox.setTag(position);
        mCheckBox.setChecked(mSparseBooleanArray.get(position));
        mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
        ImageButton settingsButton = (ImageButton)convertView.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        Intent i = new Intent(mContext, SensorDataLoggerSettings.class);
                        mContext.startActivity(i);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
        return convertView;
    }


    OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
        }
    };
}


