package day03

import println
import readInput
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {

        return input.sumOf {
            it
                .split(":")[1]
                .split("|")
                .let { game ->
                    check(game.size == 2)
                    val myNumbers = game.first()
                    val winningNumbers = game[1]


                    myNumbers.extractNumbers().intersect(winningNumbers.extractNumbers()).also { it.println() }
                }
                .let {
                    2.toDouble().pow(it.size.toDouble() - 1).toInt()
                }
                .also { it.println() }
        }


    }

    val testInput = readInput("day04/Day04_test")
    part1(testInput).also {
        it.println()
        check(it == 13)
    }

    val input = readInput("day04/Day04")
    part1(input).println()
    //part2(input).println()
}

private fun String.extractNumbers(): List<Int> = this
    .split(" ")
    .filter { it.isNotBlank() && it.first().isDigit() }
    .map { it.toInt() }
