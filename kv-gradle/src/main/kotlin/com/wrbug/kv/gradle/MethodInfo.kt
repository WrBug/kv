package com.wrbug.kv.gradle


/**
 *
 *  class: MethodInfo.kt
 *  author: wrbug
 *  date: 12/30/21
 *  description：
 *
 */
class MethodInfo {
    companion object {
        operator fun get(
            returnVoid: Boolean,
            paramsLen: Int,
            className: String,
            endCode: String = ""
        ): MethodInfo {
            return MethodInfo().apply {
                this.returnVoid = returnVoid
                this.paramsLen = paramsLen
                this.className = className
                this.endCode = endCode
            }
        }

    }

    var returnVoid = false
    var paramsLen = 0
    var className = ""
    var endCode = ""


    fun getParamsPlaceHolder(): String {
        val builder = StringBuilder()
        if (paramsLen != 0) {
            for (i in 1..paramsLen) {
                builder.append("\$$i").append(",")
            }
            builder.deleteCharAt(builder.length - 1)
        }
        return builder.toString()
    }
}