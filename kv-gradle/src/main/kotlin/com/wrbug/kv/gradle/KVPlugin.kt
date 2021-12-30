package com.wrbug.kv.gradle

import com.android.build.gradle.AppExtension
import org.gradle.api.Project


class KVPlugin : BasePlugin() {

    override fun onAppApply(project: Project) {
        project.extensions.getByType(AppExtension::class.java)
            .registerTransform(KVTransform(project))
    }
}