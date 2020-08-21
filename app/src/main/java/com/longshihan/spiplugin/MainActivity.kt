package com.longshihan.spiplugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.dada.mobile.SaveData
import com.dada.mobile.SaveData1
//import com.longshihan.spi_api.LTransformListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test()
        textsss.setOnClickListener {
            try {
//                val serviceLoader= loadTransformers(classLoader)
//                for(listener:LTransformListener in serviceLoader){
//                    Log.d("测试","测试123"+listener::class.simpleName)
//                }
                val serviceLoader=classLoader.getResources("")
                Log.d("测试","测试123")
                SaveData1.saveFirst(System.currentTimeMillis(),"","")
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
        for(index in 0..4){
            test()
        }
    }

    fun test(){
        for(index in 0..10){
            val txtView=TextView(this)
            txtView.setText("sssss")
        }
        Log.d("测试","测试123")
    }
}
