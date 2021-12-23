package com.wrbug.kv.compile

import com.wrbug.kv.annotation.KV
import com.wrbug.kv.annotation.KVDataProvider
import com.wrbug.kv.compile.runner.DataProviderCreatorTaskRunner
import com.wrbug.kv.compile.runner.DataProviderManagerTaskRunner
import com.wrbug.kv.compile.runner.KVTaskRunner
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

class KVProcessor : AbstractProcessor() {
    override fun init(p0: ProcessingEnvironment) {
        Global.messager = p0.messager
        Global.filer = p0.filer
        Global.rootDir = p0.options["rootDir"] ?: ""
        super.init(p0)
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()


    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        hashSetOf(KV::class.java.canonicalName, KVDataProvider::class.java.canonicalName)

    override fun process(p0: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(KV::class.java).forEach {
            KVTaskRunner(it).safeRun()
            DataProviderCreatorTaskRunner(it).safeRun()
        }
        roundEnv.getElementsAnnotatedWith(KVDataProvider::class.java).toList().getOrNull(0)?.let {
            DataProviderManagerTaskRunner(it).safeRun()
        }
        return true
    }
}