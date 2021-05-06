package com.longshihan.lplugin.utils

import com.longshihan.lplugin.config.LConfigInfo

object Config {

    val USER_CONIFG="lConfig"
    var dependencyVersion:String="0.9.4"
    var enable:Boolean=false
    var blackStartList= mutableSetOf<String>()
    var blackEndList= mutableSetOf<String>()
    var blackKeyList= mutableSetOf<String>()
    var blackMethodList= mutableSetOf<String>()

    fun transformConfig(info: LConfigInfo?) {
        if (info != null) {
            dependencyVersion = info.version
            enable = info.enable
            for (index in info.blackStartList){
                blackStartList.add(index)
            }
            for (index in info.blackEndList){
                blackEndList.add(index)
            }
            for (index in info.blackKeyList){
                blackKeyList.add(index)
            }
            for (index in info.blackMethodList){
                blackMethodList.add(index)
            }
            //默认
            blackStartList.add("com.longshihan.collect")
            blackStartList.add("kotlin.jvm.internal.Intrinsics")
            //结尾默认添加
            blackEndList.add("BuildConfig.class")
            blackEndList.add("R.class")
            //关键字过滤添加
            blackKeyList.add("META-INF")
            blackKeyList.add("R\$")
            //黑名单方法添加
            blackMethodList.add("checkNotNull")
            blackMethodList.add("<clinit>")
            blackMethodList.add("<init>")
            blackMethodList.add("checkExpressionValueIsNotNull")
            blackMethodList.add("checkParameterIsNotNull")
        }
    }
}