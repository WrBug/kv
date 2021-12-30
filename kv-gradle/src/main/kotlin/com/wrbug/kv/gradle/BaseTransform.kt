package com.wrbug.kv.gradle

import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation

abstract class BaseTransform : Transform() {

    override fun transform(transformInvocation: TransformInvocation?) {
        kotlin.runCatching {
            transformInvocation?.let {
                safeTransform(it)
            }
        }
    }

    abstract fun safeTransform(transformInvocation: TransformInvocation)

}