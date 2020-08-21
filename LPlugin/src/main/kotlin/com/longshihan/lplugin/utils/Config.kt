package com.longshihan.lplugin.utils

import com.longshihan.lplugin.config.LConfigInfo

object Config {

    val USER_CONIFG="lConfig"
    var dependencyVersion:String="0.6"
    var enable:Boolean=false

    fun transformConfig(info: LConfigInfo?) {
        if (info != null) {
            dependencyVersion = info.version
            enable = info.enable
        }
    }
}