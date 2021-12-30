package com.wrbug.kv.gradle

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class BasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        try {
            if (target.plugins.hasPlugin(AppPlugin::class.java)) {
                onAppApply(target)
            } else if (target.plugins.hasPlugin(LibraryPlugin::class.java)) {
                onLibraryApply(target)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    open fun onAppApply(project: Project) {

    }

    open fun onLibraryApply(project: Project) {

    }
}