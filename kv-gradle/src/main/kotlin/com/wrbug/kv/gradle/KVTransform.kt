package com.wrbug.kv.gradle

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Status
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.wrbug.kv.gradle.task.MergeDataProviderManagerTask
import com.wrbug.kv.gradle.task.MergeImplManagerTask
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import java.io.File

class KVTransform(project: Project) : BaseTransform() {

    private val tasks =
        arrayOf(MergeDataProviderManagerTask(project), MergeImplManagerTask(project))

    override fun getName() = "KVTransform"

    override fun getOutputTypes(): MutableSet<QualifiedContent.ContentType> =
        TransformManager.CONTENT_CLASS

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> =
        TransformManager.CONTENT_CLASS

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> =
        TransformManager.SCOPE_FULL_PROJECT

    override fun isIncremental(): Boolean = false

    override fun safeTransform(transformInvocation: TransformInvocation) {
        val outputProvider = transformInvocation.outputProvider
        val tmpDeleteEntryMap = HashMap<String, ArrayList<String>>()
        val realDeleteEntryMap = HashMap<String, ArrayList<String>>()
        val copyList = ArrayList<Pair<File, File>>()
        outputProvider.deleteAll()
        transformInvocation.inputs.forEach { input ->
            input.jarInputs.forEach { jarInput ->
                if (jarInput.status == Status.REMOVED) {
                    return@forEach
                }
                var destName = jarInput.name
                val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
                if (destName.endsWith(".jar")) {
                    destName = destName.substring(0, destName.length - 4)
                }
                val dest = outputProvider.getContentLocation(
                    "${destName}_${md5Name}",
                    jarInput.contentTypes,
                    jarInput.scopes,
                    Format.JAR
                )
                var success = false
                tasks.forEach {
                    success = TransformUtils.findClass(
                        jarInput,
                        it.relativeClassPath,
                        it.classPaths,
                        it.dependencyClassPaths,
                        tmpDeleteEntryMap
                    )
                }
                if (success) {
                    realDeleteEntryMap[dest.absolutePath] =
                        tmpDeleteEntryMap[jarInput.file.absolutePath] ?: arrayListOf()
                }
                copyList.add(jarInput.file to dest)
            }
            input.directoryInputs.forEach {
                copyList.forEach {
                    FileUtils.copyFile(it.first, it.second)
                }
                tasks.forEach { task ->
                    task.mergeClassFile(it.file, realDeleteEntryMap)
                }
                val dest = outputProvider.getContentLocation(
                    it.name,
                    it.contentTypes,
                    it.scopes,
                    Format.DIRECTORY
                )
                FileUtils.copyDirectory(it.file, dest)
            }
        }
    }

}