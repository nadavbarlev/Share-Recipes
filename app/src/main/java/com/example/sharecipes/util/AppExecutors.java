package com.example.sharecipes.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    /* Constants */
    private static final String TAG = "AppExecutors";

    /* Data Members */
    private static AppExecutors instance = null;
    private final ScheduledExecutorService mNetworkIO;

    /* Constructor */
    private AppExecutors() {
        mNetworkIO = Executors.newScheduledThreadPool(3);
    }

    /* Singleton */
    public static AppExecutors getInstance() {
        if (instance == null) {
            instance = new AppExecutors();
        }
        return instance;
    }

    /* Methods */
    public ScheduledExecutorService networkIO() {
        return mNetworkIO;
    }
}
