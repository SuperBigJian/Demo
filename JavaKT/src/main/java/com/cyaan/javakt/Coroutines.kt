package com.cyaan.javakt

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    println("Hello Kotlin")

    val p = test<String, Int>({
        this.toCharArray().first().code
    }, "a")
    println(p)

    getResult<String, Int>({
        this.toCharArray().first().code
    }, "a", ::test)

    GlobalScope.launch {

    }
}

fun <A, B> getResult(block: suspend A.() -> B, r: A, m: (block: suspend A.() -> B, r: A) -> Unit) {
    m.invoke(block, r)
}

fun <R, T> test(block: suspend R.() -> T, r: R): Unit {

}