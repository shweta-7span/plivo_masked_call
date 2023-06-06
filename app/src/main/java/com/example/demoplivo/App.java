package com.example.demoplivo;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.plivo.endpoint.BuildConfig;

public class App extends Application {

    private PlivoBackEnd backend;

    @Override
    public void onCreate() {
        super.onCreate();
        backend = PlivoBackEnd.newInstance();
        backend.init(BuildConfig.DEBUG);
    }

    public PlivoBackEnd backend() {
        return backend;
    }
}
