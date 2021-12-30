package com.wrbug.kv.gradle

import com.android.build.api.transform.JarInput
import com.android.build.gradle.AppExtension
import javassist.ClassPath
import javassist.ClassPool
import javassist.CtMethod
import javassist.CtNewMethod
import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


object TransformUtils {

    fun mergeClassFile(
        project: Project,
        relativeClassPath: String,
        methodInfoMap: Map<String, MethodInfo>,
        dir: File,
        classPaths: ArrayList<File>,
        routeDependencyClassPaths: List<String>,
        deleteEntryMap: HashMap<String, ArrayList<String>>,
        className: String
    ) {
        if (classPaths.isEmpty()) {
            return
        }
        val finderFile = findClassFile(dir, relativeClassPath)
        if (!finderFile.exists() || !finderFile.isFile) {
            return
        }
        deleteEntryMap.forEach { (t, u) ->
            deleteEntry(t, u)
        }
        deleteEntryMap.clear()
        classPaths.add(dir)
        val methodsMap = HashMap<String, ArrayList<CtMethod>>()
        methodInfoMap.keys.forEach { key ->
            methodsMap[key] = ArrayList()
        }
        val classPoolList = ArrayList<ClassPool>()
        val classPathList = ArrayList<ClassPath>()
        classPaths.forEach {
            val classPool = ClassPool(true)
            val classPath = classPool.insertClassPath(it.absolutePath)
            val finderClass = classPool.get(className)
            methodInfoMap.keys.forEach { methodName ->
                val method = finderClass.getDeclaredMethod(methodName)
                if (method != null) {
                    methodsMap[methodName]?.add(method)
                }
            }
            classPoolList.add(classPool)
            classPathList.add(classPath)
            classPool.removeClassPath(classPath)
        }
        methodsMap.forEach { (methodName, methods) ->
            val methodInfo = methodInfoMap[methodName] ?: return@forEach
            if (methods.isEmpty()) {
                return@forEach
            }
            val tmp = methods[0]
            val classPool = ClassPool(true)
            classPool.insertClassPath(dir.absolutePath)
            classPool.insertClassPath(getAndroidClassPath(project))
            routeDependencyClassPaths.forEach { path ->
                classPool.insertClassPath(path)
            }
            val clazz = classPool.get(className)
            val getMethod = CtNewMethod.copy(tmp, tmp.name, clazz, null)
            clazz.removeMethod(clazz.getDeclaredMethod(methodName))
            val body = StringBuilder()
            body.append("{\n")
            if (!methodInfo.returnVoid) {
                body.append("Object result = null;")
            }
            methods.forEachIndexed { index, method ->
                val newName = "${methodName}$${index}"
                method.name = newName
                clazz.addMethod(CtNewMethod.copy(method, clazz, null))
                if (!methodInfo.returnVoid) {
                    body.append("result = ${method.name}(${methodInfo.getParamsPlaceHolder()});\n")
                    body.append("if (result != null) return result;\n")
                } else {
                    body.append("${method.name}(${methodInfo.getParamsPlaceHolder()});\n")
                }
            }
            body.append(methodInfo.endCode)
            body.append("}\n")
            getMethod.setBody(body.toString())
            clazz.addMethod(getMethod)
            clazz.writeFile(dir.absolutePath)
        }
    }

    fun getAndroidClassPath(project: Project): String {
        val properties = Properties()
        val file = project.rootProject.file("local.properties")
        val sdkDir = if (file.exists()) {
            properties.load(file.inputStream())
            properties.getProperty("sdk.dir")
        } else {
            System.getenv("ANDROID_HOME")
        }
        return "${sdkDir}/platforms/${project.extensions.getByType(AppExtension::class.java).compileSdkVersion}/android.jar"
    }

    fun findClass(
        jarInput: JarInput,
        name: String,
        relativeClassPath: ArrayList<File>,
        dependencyClassPaths: ArrayList<String>,
        deleteEntryMap: HashMap<String, ArrayList<String>>
    ): Boolean {
        val jarFile = JarFile(jarInput.file)
        findEntry(jarFile, name)?.let { entry ->
            val finderFile = File(jarInput.file.parentFile, entry.name)
            finderFile.parentFile.mkdirs()
            IOUtils.copy(jarFile.getInputStream(entry), FileOutputStream(finderFile))
            relativeClassPath.add(jarInput.file.parentFile)
            val key = jarInput.file.absolutePath
            val list = deleteEntryMap[key] ?: ArrayList<String>().also {
                deleteEntryMap[key] = it
            }
            list.add(entry.name)
            return true
        }
        dependencyClassPaths.add(jarInput.file.absolutePath)
        return false
    }

    private fun findClassFile(file: File, className: String) = File(file, className)

    private fun findEntry(jarFile: JarFile, name: String): JarEntry? {
        val entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            val jarEntry = entries.nextElement()
            if (name == jarEntry.name) {
                return jarEntry
            }
        }
        return null
    }

    fun deleteEntry(path: String, entryNames: List<String>) {
        val file = File(path)
        val jarFile = JarFile(file)
        val tmpJar = File(file.parentFile, file.name + ".tmp")
        val jarOutputStream = JarOutputStream(FileOutputStream(tmpJar))
        val entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            val jarEntry = entries.nextElement()
            if (entryNames.contains(jarEntry.name)) {
                continue
            }
            val zipEntry = ZipEntry(jarEntry.name)
            val inputStream = jarFile.getInputStream(jarEntry)
            jarOutputStream.putNextEntry(zipEntry)
            jarOutputStream.write(IOUtils.toByteArray(inputStream))
            jarOutputStream.closeEntry()
        }
        IOUtils.closeQuietly(jarOutputStream)
        IOUtils.closeQuietly(jarFile)
        if (file.exists() && !file.name.contains(".gradle")) {
            file.delete()
        }
        tmpJar.renameTo(file)
    }

}