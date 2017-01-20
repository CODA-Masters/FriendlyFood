package app;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by hugo on 29/12/16.
 */

public class FriendlyFoodApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
    }

}
