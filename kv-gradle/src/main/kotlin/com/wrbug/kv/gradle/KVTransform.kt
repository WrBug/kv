package com.wrbug.kv.gradle

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Status
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import java.io.File

class KVTransform : BaseTransform() {
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
        val tmpDeleteEntryMap = HashMap<String, List<String>>()
        val realDeleteEntryMap = HashMap<String, List<String>>()
        val copyList = ArrayList<Array<File>>()
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

            }

        }
    }

}