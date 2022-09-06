package com.cyaan.javakt

import kotlin.concurrent.thread


//inline 优化代码 讲lambda函数直接插入调用出 而不是创建对象进行调用
//lambda 不在创建对象则不能作为返回值 被函数return 在lambda参数上系上noinline则 关闭函数的内联优化让函数作为对象创建并可以返回
//默认不允许lambda函数嵌套 加入crossinline可以关闭这种限制 但是同时 lambda函数不能使用 return 中断外部函数
fun main(args: Array<String>) {
    val rfun = outFun({
        ""
    }, {
        ""
    }, {
        return
    })

    rfun.invoke()
}

private inline fun outFun(crossinline block: () -> String, noinline result: () -> String, callback: () -> Unit): () -> String {
    thread {
        block.invoke()
    }
    return result
}
