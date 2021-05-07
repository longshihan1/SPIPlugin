package com.longshihan.lplugin.config

import com.longshihan.lplugin.utils.Config
import com.longshihan.lplugin.utils.Config.dependencyVersion

open class LConfigInfo {
    var version:String=dependencyVersion
    var enable:Boolean= Config.enable
    //开头过滤
    var blackStartList= mutableListOf<String>()
    //结尾过滤
    var blackEndList= mutableListOf<String>()
    //关键字过滤
    var blackKeyList= mutableListOf<String>()
    //过滤非法方法
    var blackMethodList= mutableListOf<String>()
    //白名单
    var whitePackageList= mutableListOf<String>()
    //包过滤，文件过滤，白名单，黑名单
}