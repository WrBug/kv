package com.wrbug.kv.gradle.task

import com.wrbug.kv.gradle.MethodInfo
import com.wrbug.kv.gradle.TransformUtils
import org.gradle.api.Project
import java.io.File


/**
 *
 *  class: MergeDataProviderManagerTask.kt
 *  author: wrbug
 *  date: 12/30/21
 *  description：
 *
 */
class MergeDataProviderManagerTask(project: Project) : BaseTask(project) {
    override val className: String
        get() = "com.wrbug.kv.runtime.compileonly.DataProviderManager"


    override fun mergeClassFile(dir: File, deleteEntryMap: HashMap<String, ArrayList<String>>) {
        println("MergeDataProviderManagerTask")
        TransformUtils.mergeClassFile(
            project,
            relativeClassPath,
            mapOf("getDataProvider" to MethodInfo[false, 0, className]),
            dir, classPaths, dependencyClassPaths, deleteEntryMap, className
        )

    }
}