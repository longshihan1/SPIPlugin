package com.dada.mobile;

import android.app.Application;
import androidx.multidex.MultiDex;

public class ExamApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
