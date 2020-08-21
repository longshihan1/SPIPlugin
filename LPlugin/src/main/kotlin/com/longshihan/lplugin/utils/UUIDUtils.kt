package com.longshihan.lplugin.utils

import java.util.*

class UUIDUtils {
    companion object{
        fun transformUUIDformClass(name: String):String{
            return UUID.randomUUID().toString().plus(name)
        }
    }
}