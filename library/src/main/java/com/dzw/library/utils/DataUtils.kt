package com.dzw.library.utils

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * @author Death丶Love
 * @date 2022-04-11 14:39
 * @description 关于数据处理的工具类
 */
object DataUtils {
    /**
     * kotlin 数据类 深拷贝方法
     * 注意：一定要是数据类才行啊
     * 参考：https://github.com/bennyhuo/KotlinDeepCopy
     */
    fun <T : Any> T.deepCopy(): T {
        if (!this::class.isData) {
            return this
        }

        return this::class.primaryConstructor!!.let { primaryConstructor ->
            primaryConstructor.parameters.map { parameter ->
                val value =
                    (this::class as KClass<T>).memberProperties.first { it.name == parameter.name }
                        .get(this)
                if ((parameter.type.classifier as? KClass<*>)?.isData == true) {
                    parameter to value?.deepCopy()
                } else {
                    parameter to value
                }
            }.toMap().let(primaryConstructor::callBy)
        }
    }
}