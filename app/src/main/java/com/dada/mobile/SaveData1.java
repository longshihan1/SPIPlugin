package com.dada.mobile;

import android.app.Application;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveData1 {
    private static List<String> list=new ArrayList<>();
   static SimpleDateFormat sdf=new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  static   String filename;
   static int count=0;
    public static Map<String, Long> sStartTime = new HashMap<>();



    public static void saveFirst(Long startTime,String clazzname,String methodName){
        sStartTime.put(clazzname+methodName,System.currentTimeMillis());
        count++;
    }

    public static void saveLast(Long startTime,String clazzname,String methodName){
        list.add(System.currentTimeMillis()+"："+sStartTime.get(clazzname+methodName)
                +",coast:"+(System.currentTimeMillis()-sStartTime.get(clazzname+methodName))+"ms\n");
        count++;
        if (count%30==1){
            saveData();
        }
    }

    public static void init(Application application){
        try {
            File file=application.getExternalCacheDir();
            if (!file.exists()){
                file.mkdirs();
            }
            filename=file.getAbsolutePath()+"/time-"+sdf.format(System.currentTimeMillis())+ ".txt";
            File file1=new File(filename);
            if (!file1.exists()){
                file1.createNewFile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveData(){
        FileOutputStream fos=null;
        try {
            fos=new FileOutputStream(new File(filename),true);
            FileChannel channel=fos.getChannel();
            ByteBuffer src= Charset.forName("utf8").encode(list.toString());
            int length=0;
            while (channel.write(src)!=0){ }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            count=0;
            try {
                if (fos!=null) {
                    fos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
