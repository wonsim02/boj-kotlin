package com.github.wonsim02.boj

import java.util.Scanner

/**
 * [1006번 문제](https://www.acmicpc.net/problem/1006)에 대한 답안. ([제출된 답안](http://boj.kr/84490a63740d44b78510b5362e49081c))
 */
object Problem01006 {

    private const val INFINITY: Short = 20001
    private const val MAX_N = 10000

    private const val INNER = 0
    private const val OUTER = 1

    /**
     * `1` & `n` 구역도, `n+1` & `2n` 구역도 한 팀으로 묶이지 않은 경우
     */
    private const val FIRST_CASE0 = 0
    /**
     * `n+1` & `2n` 구역이 한 팀으로 묶인 경우
     */
    private const val FIRST_CASE1 = 1
    /**
     * `1` & `n` 구역이 한 팀으로 묶인 경우
     */
    private const val FIRST_CASE2 = 2
    /**
     * `1` & `n` 구역도, `n+1` & `2n` 구역도 한 팀으로 묶인 경우
     */
    private const val FIRST_CASE3 = 3

    /**
     * `i` 및 `n+i` 구역에 대해 `i`구역도, `n+i`구역도 다른 구역과 묶이지 않은 경우
     */
    private const val CURRENT_CASE0 = 0
    /**
     * `i` 및 `n+i` 구역에 대해 `n+i` 구역이 타 구역(`n+i-1`)과 묶인 경우
     */
    private const val CURRENT_CASE1 = 1
    /**
     * `i` 및 `n+i` 구역에 대해 `i` 구역이 타 구역(`i-1`)과 묶인 경우
     */
    private const val CURRENT_CASE2 = 2
    /**
     * `i` 및 `n+i` 구역에 대해 `i` 구역과 `n+i` 구역 둘 다 타 구역과 묶인 경우. 다음 두 가지 경우가 가능하다.
     * - `i` & `n+i`
     * - `i-1` & `i`, `n+i-1` & `n+i`
     */
    private const val CURRENT_CASE3 = 3

    private val firstCases = listOf(FIRST_CASE0, FIRST_CASE1, FIRST_CASE2, FIRST_CASE3)
    private val currentCases = listOf(CURRENT_CASE0, CURRENT_CASE1, CURRENT_CASE2, CURRENT_CASE3)

    private val scanner = Scanner(System.`in`)
    private val numEnemiesInArea: Array<Array<Short>> = Array(2) { Array(MAX_N) { 0 } }
    private val minNumbers: Array<Short> = Array(MAX_N * 16) { 0 }

    private fun initNumEnemiesInArea(n: Int) {
        (INNER..OUTER).forEach { index0 ->
            (0 until n).forEach { index1 ->
                numEnemiesInArea[index0][index1] = scanner.nextShort()
            }
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun getMinNumbers(index0: Int, index1: Int, index2: Int): Short {
        return minNumbers[index0 * 16 + index1 * 4 + index2]
    }

    private inline fun setMinNumbers(
        index0: Int,
        index1: Int,
        index2: Int,
        valueGetter: () -> Short,
    ) {
        minNumbers[index0 * 16 + index1 * 4 + index2] = valueGetter()
    }

    fun solve() {
        var n: Int
        var w: Int

        var flag0: Boolean
        var flag1: Boolean
        var flag2: Boolean
        var flag3: Boolean

        var m: Short
        var m01: Short
        var m02: Short
        var result: Short

        var prev0: Short
        var prev1: Short
        var prev2: Short
        var prev3: Short

        for (t in (1..scanner.nextInt())) {
            n = scanner.nextInt()
            w = scanner.nextInt()
            initNumEnemiesInArea(n)

            if (n == 1) {
                if (numEnemiesInArea[INNER][0] + numEnemiesInArea[OUTER][0] <= w) {
                    println(1)
                } else {
                    println(2)
                }
                continue
            }

            if (n == 2) {
                flag0 = numEnemiesInArea[INNER][0] + numEnemiesInArea[OUTER][0] <= w
                flag1 = numEnemiesInArea[INNER][1] + numEnemiesInArea[OUTER][1] <= w
                flag2 = numEnemiesInArea[INNER][0] + numEnemiesInArea[INNER][1] <= w
                flag3 = numEnemiesInArea[OUTER][0] + numEnemiesInArea[OUTER][1] <= w

                if (flag0 && flag1 || flag2 && flag3) {
                    println(2)
                } else if (flag0 || flag1 || flag2 || flag3) {
                    println(3)
                } else {
                    println(4)
                }
                continue
            }

            flag0 = numEnemiesInArea[INNER][0] + numEnemiesInArea[INNER][n-1] <= w
            flag1 = numEnemiesInArea[OUTER][0] + numEnemiesInArea[OUTER][n-1] <= w
            flag2 = numEnemiesInArea[INNER][0] + numEnemiesInArea[OUTER][0] <= w

            for (index0 in firstCases) {
                for (index1 in currentCases) {
                    setMinNumbers(0, index0, index1) {
                        when {
                            index0 == FIRST_CASE0 && index1 == CURRENT_CASE3 && flag2 -> 1
                            index0 == FIRST_CASE0 && index1 == CURRENT_CASE0 -> 2
                            index0 == FIRST_CASE1 && index1 == CURRENT_CASE1 && flag1 -> 2
                            index0 == FIRST_CASE2 && index1 == CURRENT_CASE2 && flag0 -> 2
                            index0 == FIRST_CASE3 && index1 == CURRENT_CASE3 && flag0 && flag1 -> 2
                            else -> INFINITY
                        }
                    }
                }
            }

            for (i in 1 until n-1) {
                flag0 = numEnemiesInArea[INNER][i-1] + numEnemiesInArea[INNER][i] <= w
                flag1 = numEnemiesInArea[OUTER][i-1] + numEnemiesInArea[OUTER][i] <= w
                flag2 = numEnemiesInArea[INNER][i] + numEnemiesInArea[OUTER][i] <= w

                for (index0 in firstCases) {
                    prev0 = getMinNumbers(i-1, index0, CURRENT_CASE0)
                    prev1 = getMinNumbers(i-1, index0, CURRENT_CASE1)
                    prev2 = getMinNumbers(i-1, index0, CURRENT_CASE2)
                    prev3 = getMinNumbers(i-1, index0, CURRENT_CASE3)

                    m = minOf(prev0, prev1, prev2, prev3)
                    setMinNumbers(i, index0, CURRENT_CASE0) {
                        if (m != INFINITY) {
                            (m + 2).toShort()
                        } else {
                            INFINITY
                        }
                    }

                    m02 = minOf(prev0, prev2)
                    setMinNumbers(i, index0, CURRENT_CASE1) {
                        if (m02 != INFINITY && flag1) {
                            (m02 + 1).toShort()
                        } else {
                            INFINITY
                        }
                    }

                    m01 = minOf(prev0, prev1)
                    setMinNumbers(i, index0, CURRENT_CASE2) {
                        if (m01 != INFINITY && flag0) {
                            (m01 + 1).toShort()
                        } else {
                            INFINITY
                        }
                    }

                    setMinNumbers(i, index0, CURRENT_CASE3) {
                        minOf(
                            if (m != INFINITY && flag2) {
                                (m + 1).toShort()
                            } else {
                                INFINITY
                            },
                            if (prev0 != INFINITY && flag0 && flag1) {
                                prev0
                            } else {
                                INFINITY
                            },
                        )
                    }
                }
            }

            flag0 = numEnemiesInArea[INNER][n-2] + numEnemiesInArea[INNER][n-1] <= w
            flag1 = numEnemiesInArea[OUTER][n-2] + numEnemiesInArea[OUTER][n-1] <= w
            flag2 = numEnemiesInArea[INNER][n-1] + numEnemiesInArea[OUTER][n-1] <= w

            result = INFINITY
            for (index0 in firstCases) {
                prev0 = getMinNumbers(n-2, index0, CURRENT_CASE0)
                prev1 = getMinNumbers(n-2, index0, CURRENT_CASE1)
                prev2 = getMinNumbers(n-2, index0, CURRENT_CASE2)
                prev3 = getMinNumbers(n-2, index0, CURRENT_CASE3)

                m = minOf(prev0, prev1, prev2, prev3)
                m01 = minOf(prev0, prev1)
                m02 = minOf(prev0, prev2)

                if (index0 == FIRST_CASE0) {
                    result = minOf(result, (m + 2).toShort())
                    if (m != INFINITY && flag2) {
                        result = minOf(result, (m + 1).toShort())
                    }
                    if (m02 != INFINITY && flag1) {
                        result = minOf(result, (m02 + 1).toShort())
                    }
                    if (m01 != INFINITY && flag0) {
                        result = minOf(result, (m01 + 1).toShort())
                    }
                    if (prev0 != INFINITY && flag0 && flag1) {
                        result = minOf(result, prev0)
                    }
                } else if (index0 == FIRST_CASE1) {
                    result = minOf(result, (m + 1).toShort())
                    if (m01 != INFINITY && flag0) {
                        result = minOf(result, m01)
                    }
                } else if (index0 == FIRST_CASE2) {
                    result = minOf(result, (m + 1).toShort())
                    if (m02 != INFINITY && flag1) {
                        result = minOf(result, m02)
                    }
                } else {
                    result = minOf(result, m)
                }
            }
            println(result)
        }
    }
}

fun main() {
    Problem01006.solve()
}
