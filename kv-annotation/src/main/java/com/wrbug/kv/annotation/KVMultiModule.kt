package com.wrbug.kv.annotation


/**
 * 仅用于触发app module apt生成。
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class KVMultiModule
