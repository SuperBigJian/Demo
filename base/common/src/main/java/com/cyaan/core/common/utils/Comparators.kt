package com.cyaan.core.common.utils

/**
 * 验证比较器的传递性 即 a>b ,b>c 则 a>c
 */
fun <T> verifyTransitivity(comparator: Comparator<T>, elements: Collection<T>) {
    for (first in elements) {
        for (second in elements) {
            val result1 = comparator.compare(first, second)
            val result2 = comparator.compare(second, first)
            if (result1 != -result2) {
                throw AssertionError("compare($first,$second) == $result1 but swapping the parameters returns $result2")
            }
        }
    }
    for (first in elements) {
        for (second in elements) {
            val firstGreaterThanSecond = comparator.compare(first, second)
            if (firstGreaterThanSecond <= 0) continue
            for (third in elements) {
                val secondGreaterThanThird = comparator.compare(second, third)
                if (secondGreaterThanThird <= 0) continue
                val firstGreaterThanThird = comparator.compare(first, third)
                if (firstGreaterThanThird <= 0) {
                    throw AssertionError("compare($first,$second) > 0, compare($second,$third) > 0, but compare($first,$third) == $firstGreaterThanThird")
                }
            }
        }
    }
}