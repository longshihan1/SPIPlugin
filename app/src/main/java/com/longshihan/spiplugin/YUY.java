package com.longshihan.spiplugin;

import android.util.Log;
import com.dada.mobile.SaveData;

public class YUY {
    public static void Yu(){
        SaveData.saveFirst(System.currentTimeMillis(),"123","123");
        Log.d("测试","测试");
        SaveData.saveLast(System.currentTimeMillis(),"123","123");

    }
}
