package com.longshihan.lplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.longshihan.lplugin.config.LConfig
import com.longshihan.lplugin.dependencies.ArgusDependencyResolutionListener
import com.longshihan.lplugin.utils.Config
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
    override fun apply(project: Project) {
        when{
            project.plugins.hasPlugin(AppPlugin::class.java)->project.getAndroid<AppExtension>().let { android->
//                val serviceLoader: ServiceLoader<LTransformListener> = ServiceLoader.load(LTransformListener::class.java)
//                val variants=android.applicationVariants
                //check release and debug
//                project.extensions.create(Config.USER_CONIFG, LConfig::class.java)
                project.gradle.addListener(ArgusDependencyResolutionListener(project))
//                println("----------加载成功${lTransformListener.size}--------------------")
               val lTransform=LTransform(project)
//                for (service:LTransformListener in serviceLoader){
//                    println("-----"+service::class.simpleName)
//                    lTransform.transformers.addAll(lTransformListener)
//                }
                android.registerTransform(lTransform)
            }
        }

    }

}