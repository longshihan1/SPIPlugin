package com.longshihan.lplugin.dependencies

import com.longshihan.lplugin.utils.Config
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyResolutionListener
import org.gradle.api.artifacts.ResolvableDependencies
val COMPILE_CONFIGURATIONS = arrayOf("api", "compile")
/**
 * 兼容Compile模式
 */
fun Project.compatCompile(depLib: Any) {
    COMPILE_CONFIGURATIONS.find { configurations.findByName(it) != null }?.let {
        dependencies.add(it, depLib)
    }
}

class ArgusDependencyResolutionListener(private val project: Project):DependencyResolutionListener{
    override fun beforeResolve(p0: ResolvableDependencies) {
        println("时间插桩插件----依赖添加")
        project.compatCompile("com.github.longshihan1:DataCollect:".plus(Config.dependencyVersion))
        project.gradle.removeListener(this)
    }

    override fun afterResolve(p0: ResolvableDependencies) {

    }

}