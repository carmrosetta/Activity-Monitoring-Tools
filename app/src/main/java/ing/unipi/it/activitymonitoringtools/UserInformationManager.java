package ing.unipi.it.activitymonitoringtools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * @file UserInformationManager.java
 * @brief Java class that helps in managing user information about his/her gender, age, height and
 * weight.
 *
 * This class is used to save user information in key-value pairs.
 * This information is saved persistently in a xml file, that is it will persist across user
 * sessions, even if the application is killed.
 * This class is also used to retrieve, update and delete user information.
 */

public class UserInformationManager {

    public final static String USER_SEX = "userSex";
    public final static String USER_AGE = "userAge";
    public final static String USER_HEIGHT = "userHeight";
    public final static String USER_WEIGHT = "userWeight";
    public final static String USER_INFO_SAVED = "userInfoProvided";
    private final static String PREF_NAME = "UserInformation";

    SharedPreferences userInformation;
    SharedPreferences.Editor editor;
    Context _context;

    /**
     * Constructor
     *
     * @param context   Global information about the application environment (application context)
     * It allows access to application-specific resources and classes
     */

    public UserInformationManager(Context context) {
        this._context = context;
        userInformation = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = userInformation.edit();
    }

    /**
     * @brief This method saves user information in a xml file in key-value pairs
     * @param userSex       String that specifies user gender
     * @param userAge       String that specifies user age
     * @param userHeight    String that specifies user height in centimeters
     * @param userWeight    String that specifies user weight in kilograms
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
     * @brief Method that checks if the user has provided information about his/her gender, age,
     *        height and weight
     * @return true if the user has provided his/her information, false otherwise
     *
     */
    public boolean userInformationSaved() {
        return userInformation.getBoolean(USER_INFO_SAVED, false);
    }

    /**
     * @brief Method that starts UserInformationActivity to let the user provide his/her information
     */
    public void checkUserInformationSaved() {
        if (!this.userInformationSaved()) {
            Intent intent = new Intent(_context, UserInformationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            _context.startActivity(intent);
        }
    }

    /**
     * @brief Method that gets user information
     * @return userInfo HashMap<String, String> that contains user information in key-value pairs
     *
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
     * @brief Method that clears user information
     */
    public void clearUserInformation() {

        editor.clear();
        editor.commit();

        Intent intent = new Intent(_context, UserInformationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);

    }


}
