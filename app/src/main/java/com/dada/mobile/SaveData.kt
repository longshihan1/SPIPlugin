package com.dada.mobile

import android.app.Application
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.text.SimpleDateFormat

object SaveData {
    val list= mutableListOf<String>()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var filename=""
    var count=0
    @JvmStatic
    fun saveFirst(startTime: Long,clazzname:String,methodName:String){
        //输出当前的名字和时间
        val string= sdf.format(startTime)
        list.add("$clazzname:$methodName-开始时间$string\n")
        count++
        if (count %3==1){
            saveData()
        }
    }
    @JvmStatic
    fun saveLast(startTime: Long,clazzname:String,methodName:String){
        val string= sdf.format(System.currentTimeMillis())
        list.add("$clazzname:$methodName-结束时间$string，耗时：${System.currentTimeMillis()-startTime}ms\n")
    }
    @JvmStatic
    fun saveData(){
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(File(filename), true)
            val channel: FileChannel = fos.channel
            val src: ByteBuffer = Charset.forName("utf8").encode(list.toString())
            var length = 0
            while (channel.write(src).also { length = it } != 0) {
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            count =0
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @JvmStatic
    fun init(appplication:Application){
        try {
        val file: File = appplication.externalCacheDir ?: return
        if (!file.exists()) {
            file.mkdirs()
        }
        filename = file.absolutePath.toString() + "/time-" + sdf.format(System.currentTimeMillis()) + ".txt"
        val file1 = File(filename)
        if (!file1.exists()) {
            file1.createNewFile()
        }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun getSS(){
        print("测试")
    }
}