package com.wrbug.kv.gradle.task

import org.gradle.api.Project
import java.io.File


/**
 *
 *  class: BaseTask.kt
 *  author: wrbug
 *  date: 12/30/21
 *  description：
 *
 */
abstract class BaseTask(val project: Project) {
    val dependencyClassPaths = ArrayList<String>()
    val classPaths = ArrayList<File>()
    val relativeClassPath: String by lazy {
        className.replace(".", "/") + ".class"
    }

    abstract val className: String

    abstract fun mergeClassFile(dir: File)
}