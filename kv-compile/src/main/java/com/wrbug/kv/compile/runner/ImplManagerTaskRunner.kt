package com.wrbug.kv.compile.runner

import com.squareup.javapoet.*
import com.wrbug.kv.compile.Global
import com.wrbug.kv.compile.util.RUNTIME_ONLY_PACKAGE
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

class ImplManagerTaskRunner {
    companion object {
        private const val CLASS_IMPL_MANAGER = "ImplManager"
        private const val METHOD_MATCH_AND_GET = "matchAndGet"
        private const val FIELD_CACHE = "cache"
        private const val PARAMETER_CLAZZ = "clazz"
    }

    private val matchAndGetMethodBuilder = MethodSpec.methodBuilder(METHOD_MATCH_AND_GET)
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(Class::class.java, PARAMETER_CLAZZ)
        .returns(Any::class.java)
    private val spec = TypeSpec.classBuilder(CLASS_IMPL_MANAGER)
        .addModifiers(Modifier.PUBLIC)
        .addField(
            FieldSpec.builder(
                ParameterizedTypeName.get(
                    Map::class.java,
                    Class::class.java,
                    Any::class.java
                ), FIELD_CACHE, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL
            ).initializer("new \$T()", HashMap::class.java).build()
        ).addMethod(
            MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Class::class.java, PARAMETER_CLAZZ)
                .returns(Any::class.java)
                .beginControlFlow("if(\$L.containsKey(\$L))", FIELD_CACHE, PARAMETER_CLAZZ)
                .addStatement("return \$L.get(\$L)", FIELD_CACHE, PARAMETER_CLAZZ)
                .endControlFlow()
                .addStatement(
                    "\$T obj=\$L(\$L)",
                    Any::class.java,
                    METHOD_MATCH_AND_GET,
                    PARAMETER_CLAZZ
                )
                .addStatement("\$L.put(\$L, obj)", FIELD_CACHE, PARAMETER_CLAZZ)
                .addStatement("return obj")
                .build()
        )

    fun addElement(element: Element) {
        val type = TypeName.get(element.asType()) as ClassName
        matchAndGetMethodBuilder.beginControlFlow(
            "if (\$L==\$T.class)",
            PARAMETER_CLAZZ, type
        )
            .addStatement(
                "return \$L.\$L()",
                ClassName.get(type.packageName(), type.simpleName() + KVImplTaskRunner.SUFFIX),
                KVImplTaskRunner.METHOD_GET_INSTANCE
            )
            .endControlFlow()
    }

    fun run() {
        spec.addMethod(matchAndGetMethodBuilder.addStatement("return null").build())
        val javaFile =
            JavaFile.builder(RUNTIME_ONLY_PACKAGE, spec.build())
                .build()
        runCatching {
            javaFile.writeTo(Global.filer)
        }
    }
}