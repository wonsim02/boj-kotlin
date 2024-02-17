package com.github.wonsim02.boj

/**
 * [1006번 문제](https://www.acmicpc.net/problem/1006)에 대한 답안. ([제출된 답안](http://boj.kr/84490a63740d44b78510b5362e49081c))
 * 1. `i`, `n+i` 둘 다 사용 가능
 * 1. `i`만 사용 가능
 * 1. `n+i`만 사용 가능
 * 1. `i`, `n+i` 둘 다 사용 불가능
 */
object Problem01006 {

    private const val INFINITY = 20001

    private const val INNER = 0
    private const val OUTER = 1

    fun solve() {
        var flag0: Boolean
        var flag1: Boolean
        var flag2: Boolean
        var flag3: Boolean

        var m: Int
        var m01: Int
        var m02: Int
        var result: Int

        var prev0: Int
        var prev1: Int
        var prev2: Int

        var previousMinNumbers: List<Int>
        var currentMinNumbers: MutableList<Int>

        for (t in (1..readLine()!!.toInt())) {
            val (n, w) = readLine()!!
                .split(" ", ignoreCase = false, limit = 2)
                .map { it.toInt() }

            val numEnemiesInArea = (1..2).map {
                readLine()!!
                    .split(" ", ignoreCase = false, limit = n)
                    .map { it.toInt() }
            }

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

            val minNumbers = List(n-1) { List(4) { ArrayList<Int>(4) } }
            flag0 = numEnemiesInArea[INNER][0] + numEnemiesInArea[INNER][n-1] <= w
            flag1 = numEnemiesInArea[OUTER][0] + numEnemiesInArea[OUTER][n-1] <= w
            flag2 = numEnemiesInArea[INNER][0] + numEnemiesInArea[OUTER][0] <= w

            for (index0 in 0 until 4) {
                for (index1 in 0 until 4) {
                    minNumbers[0][index0].add(
                        when {
                            index0 == 0 && index1 == 3 && flag2 -> 1
                            index0 == 0 && index1 == 0 -> 2
                            index0 == 1 && index1 == 1 && flag1 -> 2
                            index0 == 2 && index1 == 2 && flag0 -> 2
                            index0 == 3 && index1 == 3 && flag0 && flag1 -> 2
                            else -> INFINITY
                        }
                    )
                }
            }

            for (i in 1 until n-1) {
                flag0 = numEnemiesInArea[INNER][i-1] + numEnemiesInArea[INNER][i] <= w
                flag1 = numEnemiesInArea[OUTER][i-1] + numEnemiesInArea[OUTER][i] <= w
                flag2 = numEnemiesInArea[INNER][i] + numEnemiesInArea[OUTER][i] <= w

                for (index0 in 0 until 4) {
                    previousMinNumbers = minNumbers[i-1][index0]
                    currentMinNumbers = minNumbers[i][index0]

                    prev0 = previousMinNumbers[0]
                    prev1 = previousMinNumbers[1]
                    prev2 = previousMinNumbers[2]

                    m = minOf(prev0, prev1, prev2, previousMinNumbers[3])
                    currentMinNumbers.add(
                        if (m != INFINITY) {
                            m + 2
                        } else {
                            INFINITY
                        }
                    )

                    m02 = minOf(prev0, prev2)
                    currentMinNumbers.add(
                        if (m02 != INFINITY && flag1) {
                            m02 + 1
                        } else {
                            INFINITY
                        }
                    )

                    m01 = minOf(prev0, prev1)
                    currentMinNumbers.add(
                        if (m01 != INFINITY && flag0) {
                            m01 + 1
                        } else {
                            INFINITY
                        }
                    )

                    currentMinNumbers.add(
                        minOf(
                            if (m != INFINITY && flag2) {
                                m + 1
                            } else {
                                INFINITY
                            },
                            if (prev0 != INFINITY && flag0 && flag1) {
                                prev0
                            } else {
                                INFINITY
                            },
                        )
                    )
                }
            }

            flag0 = numEnemiesInArea[INNER][n-2] + numEnemiesInArea[INNER][n-1] <= w
            flag1 = numEnemiesInArea[OUTER][n-2] + numEnemiesInArea[OUTER][n-1] <= w
            flag2 = numEnemiesInArea[INNER][n-1] + numEnemiesInArea[OUTER][n-1] <= w

            result = INFINITY
            for (index0 in 0 until 4) {
                previousMinNumbers = minNumbers[n-2][index0]

                prev0 = previousMinNumbers[0]
                prev1 = previousMinNumbers[1]
                prev2 = previousMinNumbers[2]

                m = minOf(prev0, prev1, prev2, previousMinNumbers[3])
                m01 = minOf(prev0, prev1)
                m02 = minOf(prev0, prev2)

                if (index0 == 0) {
                    result = minOf(result, m + 2)
                    if (m != INFINITY && flag2) {
                        result = minOf(result, m + 1)
                    }
                    if (m02 != INFINITY && flag1) {
                        result = minOf(result, m02 + 1)
                    }
                    if (m01 != INFINITY && flag0) {
                        result = minOf(result, m01 + 1)
                    }
                    if (prev0 != INFINITY && flag0 && flag1) {
                        result = minOf(result, prev0)
                    }
                } else if (index0 == 1) {
                    result = minOf(result, m + 1)
                    if (m01 != INFINITY && flag0) {
                        result = minOf(result, m01)
                    }
                } else if (index0 == 2) {
                    result = minOf(result, m + 1)
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
