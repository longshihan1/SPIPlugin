package com.longshihan.lplugin.utils

import com.longshihan.lplugin.config.LConfigInfo

object Config {

    val USER_CONIFG="lConfig"
    var dependencyVersion:String="0.6"
    var enable:Boolean=false
    var blackStartList= mutableSetOf<String>()
    var blackEndList= mutableSetOf<String>()
    var blackKeyList= mutableSetOf<String>()
    var blackMethodList= mutableSetOf<String>()
    var whitePackageList= mutableSetOf<String>()

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

            for (index in info.whitePackageList){
                whitePackageList.add(index)
            }
            //默认
            blackStartList.add("com.longshihan.collect")
            blackStartList.add("kotlin.jvm.internal.Intrinsics")
            blackStartList.add("com.android.builder")
            blackStartList.add("java.lang.invoke")
            blackStartList.add("com.google.auto")
            blackStartList.add("androidx.annotation")
            blackStartList.add("androidx.core.animation.AnimatorKt")
            blackStartList.add("org.checkerframework.checker")
            blackStartList.add("com.squareup.javapoet")
            //结尾默认添加
            blackEndList.add("BuildConfig.class")
            blackEndList.add("R.class")
            //关键字过滤添加
            blackKeyList.add("META-INF")
            blackKeyList.add("R\$")
            blackKeyList.add("annotations")
            blackKeyList.add("intellij")
            //黑名单方法添加
            blackMethodList.add("checkNotNull")
            blackMethodList.add("<clinit>")
            blackMethodList.add("<init>")
            blackMethodList.add("checkExpressionValueIsNotNull")
            blackMethodList.add("checkParameterIsNotNull")
            blackMethodList.add("_\$_findCachedViewById")
            blackMethodList.add("_\$_clearFindViewByIdCache")
        }
    }
}