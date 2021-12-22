package com.wrbug.kv.compile.util

import java.lang.StringBuilder


fun String.toFirstLowerCase(): String {
    if (this.isEmpty()) {
        return this
    }
    return StringBuilder().append(this[0].toLowerCase()).append(substring(1)).toString()
}