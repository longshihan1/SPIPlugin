package com.longshihan.spiplugin;

import android.app.Application;
import com.dada.mobile.SaveData1;

public class DApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SaveData1.init(this);
    }
}
