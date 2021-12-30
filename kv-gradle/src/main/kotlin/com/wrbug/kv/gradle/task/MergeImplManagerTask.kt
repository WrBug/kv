package com.wrbug.kv.gradle.task

import com.wrbug.kv.gradle.MethodInfo
import com.wrbug.kv.gradle.TransformUtils
import org.gradle.api.Project
import java.io.File


/**
 *
 *  class: MergeImplManagerTask.kt
 *  author: wrbug
 *  date: 12/30/21
 *  description：
 *
 */
class MergeImplManagerTask(project: Project) : BaseTask(project) {
    override val className: String
        get() = "com.wrbug.kv.runtime.compileonly.ImplManager"

    override fun mergeClassFile(dir: File) {
        TransformUtils.mergeClassFile(
            project,
            relativeClassPath,
            mapOf("matchAndGet" to MethodInfo[false, 1, className, "return null;"]),
            dir, classPaths, dependencyClassPaths, className
        )

    }
}