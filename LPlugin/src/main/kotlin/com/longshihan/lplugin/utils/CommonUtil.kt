package com.longshihan.lplugin.utils

import java.io.ByteArrayOutputStream
import java.io.InputStream

object CommonUtil {

    fun transformBytes(inputStream: InputStream):ByteArray{
        val baos=ByteArrayOutputStream()
        inputStream.use {
            it.copyTo(baos)
        }
        return baos.toByteArray()
    }
}