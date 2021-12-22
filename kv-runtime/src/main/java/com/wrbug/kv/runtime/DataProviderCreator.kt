package com.wrbug.kv.runtime

/**
 *
 *  class: DataProviderCreator.kt
 *  author: wrbug
 *  date: 2021/12/22
 *  description：
 *
 */
interface DataProviderCreator {
    fun newInstance(): Any
}