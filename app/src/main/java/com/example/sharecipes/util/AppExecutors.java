package com.example.sharecipes.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    /* Constants */
    private static final String TAG = "AppExecutors";

    /* Data Members */
    private final Executor mBackgroundThread;
    private final Executor mMainThread;

    // DO NOT NEED ANOTHER BACKGROUND THREAD TO DO THE NETWORK REQUEST BECAUSE
    // LIVE DATA AUTOMATICALLY DOES THEM ASYNC

    /* Constructor */
    private AppExecutors() {
        mBackgroundThread = Executors.newSingleThreadExecutor();
        mMainThread = new MainThreadExecutor();
    }

    /* Singleton */
    private static AppExecutors instance = null;
    public static AppExecutors getInstance() {
        if (instance == null) {
            instance = new AppExecutors();
        }
        return instance;
    }

    /* Getter and Setter */
    public Executor background() {
        return mBackgroundThread;
    }

    public Executor main() {
        return mMainThread;
    }

    /* Class - MainThreadExecutor */
    private class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
