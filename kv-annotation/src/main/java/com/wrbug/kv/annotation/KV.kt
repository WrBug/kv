package com.wrbug.kv.annotation


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class KV(val value: String = "", val scope: String = "")
