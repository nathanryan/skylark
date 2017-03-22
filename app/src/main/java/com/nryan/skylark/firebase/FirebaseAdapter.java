package com.nryan.skylark.firebase;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Nathan Ryan x13448212 on 10/02/2017.
 */

public class FirebaseAdapter extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }


    }
}
