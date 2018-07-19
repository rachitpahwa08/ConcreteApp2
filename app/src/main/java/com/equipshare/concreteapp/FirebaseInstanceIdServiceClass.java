package com.equipshare.concreteapp;

import android.util.Log;

import com.equipshare.concreteapp.utils.SessionManagement;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Jarvis on 15-07-2018.
 */

public class FirebaseInstanceIdServiceClass extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("FirebaseInstanceService", "Firebase New Token: " + refreshedToken);
        SessionManagement sessionManagement;
        sessionManagement=new SessionManagement(getApplicationContext());
        sessionManagement.addfirebasetoken(refreshedToken);
    }
}
