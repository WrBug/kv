package com.wrbug.kv.compile

import com.wrbug.kv.annotation.KV
import com.wrbug.kv.annotation.KVDataProvider
import com.wrbug.kv.compile.runner.DataProviderCreatorTaskRunner
import com.wrbug.kv.compile.runner.DataProviderManagerTaskRunner
import com.wrbug.kv.compile.runner.ImplManagerTaskRunner
import com.wrbug.kv.compile.runner.KVImplTaskRunner
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

class KVProcessor : AbstractProcessor() {
    override fun init(p0: ProcessingEnvironment) {
        Global.messager = p0.messager
        Global.filer = p0.filer
        super.init(p0)
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()


    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        hashSetOf(KV::class.java.canonicalName, KVDataProvider::class.java.canonicalName)

    override fun process(p0: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        val runner = ImplManagerTaskRunner()
        roundEnv.getElementsAnnotatedWith(KV::class.java).forEach {
            KVImplTaskRunner(it).safeRun()
            runner.addElement(it)
//            DataProviderCreatorTaskRunner(it).safeRun()
        }
        runner.run()
        roundEnv.getElementsAnnotatedWith(KVDataProvider::class.java).toList().getOrNull(0)?.let {
            DataProviderManagerTaskRunner(it).safeRun()
        }
        return true
    }
}