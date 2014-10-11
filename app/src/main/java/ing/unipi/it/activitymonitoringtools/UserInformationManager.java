package ing.unipi.it.activitymonitoringtools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by carmen on 11/10/14.
 */
public class UserInformationManager {

    private final static String PREF_NAME = "UserPreferences";

    public final static String USER_SEX = "userSex";
    public final static String USER_AGE = "userAge";
    public final static String USER_HEIGHT = "userHeight";
    public final static String USER_WEIGHT = "userWeight";

    public final static String USER_INFO_SAVED = "userInfoProvided";


    SharedPreferences userInformation;
    SharedPreferences.Editor editor;
    Context _context;

    /**
     * Constructor
     * @param context
     */

    public UserInformationManager(Context context) {
        this._context = context;
        userInformation = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = userInformation.edit();
    }

    /**
     * Save user preferences
     * @param userSex
     * @param userAge
     * @param userHeight
     * @param userWeight
     */
    public void saveUserInformation(String userSex, String userAge, String userHeight, String userWeight) {

        editor.putBoolean(USER_INFO_SAVED, true);
        editor.putString(USER_SEX, userSex);
        editor.putString(USER_AGE, userAge);
        editor.putString(USER_HEIGHT, userHeight);
        editor.putString(USER_WEIGHT, userWeight);

        editor.commit();

    }

    /**
     * Check if the user has provided his information
     * @return true if the user has provided his information
     */

    public boolean userInformationSaved() {
        return userInformation.getBoolean(USER_INFO_SAVED, false);
    }

    /**
     * Start UserInformationActivity to let the user provide his information
     */
    public void checkUserInformationSaved() {
        if(!this.userInformationSaved()) {
            Intent intent = new Intent(_context, UserInformationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            _context.startActivity(intent);
        }
    }

    /**
     * Get user information
     * @return userInfo
     */
    public HashMap<String, String> getUserInformation() {

        HashMap<String, String> userInfo = new HashMap<String, String>();

        userInfo.put(USER_SEX, userInformation.getString(USER_SEX, null));
        userInfo.put(USER_AGE, userInformation.getString(USER_AGE, null));
        userInfo.put(USER_HEIGHT, userInformation.getString(USER_HEIGHT, null));
        userInfo.put(USER_WEIGHT, userInformation.getString(USER_WEIGHT, null));

        return userInfo;

    }

    /**
     * Clear user information
     */
    public void clearUserInformation(){

        editor.clear();
        editor.commit();

        Intent intent = new Intent(_context, UserInformationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);

    }





}
