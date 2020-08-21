package com.longshihan.lplugin.config

import com.longshihan.lplugin.utils.Config
import com.longshihan.lplugin.utils.Config.dependencyVersion

open class LConfigInfo {
    var version:String=dependencyVersion
    var enable:Boolean= Config.enable
    //包过滤，文件过滤，白名单，黑名单
}