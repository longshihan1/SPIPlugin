package com.longshihan.lplugin.utils

import com.longshihan.spi_api.LTransformListener
import java.io.File
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Created by longhe on 2020-07-16.
 */
@Throws(ServiceConfigurationError::class)
internal fun loadTransformers(classLoader: ClassLoader) =
    newServiceLoader<LTransformListener>(classLoader).load()

internal inline fun <reified T> newServiceLoader(classLoader: ClassLoader) =
    ServiceLoaderFactory(classLoader, T::class.java).newServiceLoader()

internal class ServiceLoaderFactory<T>(
    private val classLoader: ClassLoader,
    private val service: Class<T>
) {
    fun newServiceLoader() = object : ServiceLoader<T> {
        @Suppress("UNCHECKED_CAST")
        override fun load(): List<T> {
            try {
                println("-----------进来了；${service.name} -----${classLoader.getResource("META-INF/services")}-------")
                val providerClass = Class.forName("com.longshihan.spiplugin.Test12")
                println("-----------进来了；${providerClass.simpleName} ----------")
                val resources=Thread.currentThread().contextClassLoader.getResources("META-INF/services")
                println(resources.nextElement().path)
                println("----------${classLoader.getResources("")}-${resources.hasMoreElements()}--${classLoader.getResources("META-INF/services/${service.name}")}-")
                val list =
                    classLoader.getResources("META-INF/services/${service.name}")?.asSequence()
                        ?.map(::parse)?.flatten()?.toSet()?.map { provider ->
                        try {
                            println("-----------开始---${provider}--------")
                            val providerClass = Class.forName(provider, false, classLoader)
                            println("===============" + providerClass.simpleName)
                            if (!service.isAssignableFrom(providerClass)) {
                                throw ServiceConfigurationError("Provider $provider not a subtype")
                            }

                            try {
                                providerClass.getConstructor().newInstance() as T
                            } catch (e: NoSuchMethodException) {
                                providerClass.newInstance() as T
                            }
                        } catch (e1: ClassNotFoundException) {
                            println("-----------失败：${e1.message}-----------")
                            throw ServiceConfigurationError("Provider $provider not found" + e1.message)
                        }
                    } ?: emptyList()
                return list
            } catch (e: Exception) {
                e.printStackTrace()
                return emptyList()
            }
        }

    }

}

internal interface ServiceLoader<T> {
    fun load(): List<T>
}

@Throws(ServiceConfigurationError::class)
private fun parse(u: URL) = try {
    println("------${u.path}-----------")
    u.openStream().bufferedReader(StandardCharsets.UTF_8).readLines().filter {
        it.isNotEmpty() && it.isNotBlank() && !it.startsWith('#')
    }.map(String::trim).filter(::isJavaClassName)
} catch (e: Throwable) {
    emptyList<String>()
}

private fun isJavaClassName(text: String): Boolean {
    if (!Character.isJavaIdentifierStart(text[0])) {
        throw ServiceConfigurationError("Illegal provider-class name: $text")
    }

    for (i in 1 until text.length) {
        val cp = text.codePointAt(i)
        if (!Character.isJavaIdentifierPart(cp) && cp != '.'.toInt()) {
            throw ServiceConfigurationError("Illegal provider-class name: $text")
        }
    }

    return true
}