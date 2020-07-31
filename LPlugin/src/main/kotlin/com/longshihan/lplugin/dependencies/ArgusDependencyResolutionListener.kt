package com.longshihan.lplugin.dependencies

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

class ArgusDependencyResolutionListener(val project: Project):DependencyResolutionListener{
    override fun beforeResolve(p0: ResolvableDependencies) {
        project.compatCompile("com.github.longshihan1:DataCollect:0.03")
        project.gradle.removeListener(this)
    }

    override fun afterResolve(p0: ResolvableDependencies) {

    }

}