package com.wrbug.kv.gradle

import com.android.build.api.transform.JarInput
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile


object TransformUtils {

    fun findClass(
        jarInput: JarInput,
        name: String,
        finderClassPaths: ArrayList<String>,
        dependencyClassPaths: ArrayList<String>,
        deleteEntryMap: HashMap<String, ArrayList<String>>
    ): Boolean {
        val jarFile = JarFile(jarInput.file)
        findEntry(jarFile, name)?.let { entry ->
            val finderFile = File(jarInput.file.parentFile, entry.name)
            finderFile.parentFile.mkdirs()
            IOUtils.copy(jarFile.getInputStream(entry), FileOutputStream(finderFile))
            finderClassPaths.add(jarInput.file.parentFile.path)
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


}