package com.longshihan.lplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.longshihan.lplugin.config.LConfig
import com.longshihan.lplugin.config.LConfigInfo
import com.longshihan.lplugin.dependencies.ArgusDependencyResolutionListener
import com.longshihan.lplugin.utils.Config
import com.longshihan.lplugin.utils.Config.USER_CONIFG
import com.longshihan.lplugin.utils.getAndroid
import com.longshihan.lplugin.utils.loadTransformers
import com.longshihan.spi_api.LTransformListener
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.*


/**
 * Created by longhe on 2020-07-16.
 * plugin - start
 */
public class LPlugin :Plugin<Project>{
    var info:LConfigInfo?=null
    override fun apply(project: Project) {
        when{
            project.plugins.hasPlugin(AppPlugin::class.java)->project.getAndroid<AppExtension>().let { android->
                info=project.extensions.create(USER_CONIFG,LConfigInfo::class.java)
                project.afterEvaluate {
                    info= project.extensions.getByName(USER_CONIFG) as LConfigInfo?
                    Config.transformConfig(info)
                    println("时间插桩配置获取")
                }
                project.gradle.addListener(ArgusDependencyResolutionListener(project))
                android.registerTransform(LTransform(project))
            }
        }

    }

}