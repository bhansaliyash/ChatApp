package com.example.yash.firecast1;

import com.firebase.client.Firebase;

/**
 * Created by Yash on 9/22/2016.
 */
public class FireCast1 extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}