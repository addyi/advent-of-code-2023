package day04

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


                    myNumbers.extractNumbers().intersect(winningNumbers.extractNumbers())
                }
                .let {
                    2.toDouble().pow(it.size.toDouble() - 1).toInt()
                }
        }


    }

    fun part2(input: List<String>): Int {
        return input.mapIndexed { index, _ ->
            numOfWinningCards(input, index)
        }.sum()
    }

    val testInput = readInput("day04/Day04_test")
    part1(testInput).also {
        it.println()
        check(it == 13)
    }

    part2(testInput).also {
        it.println()
        check(it == 30)
    }

    val input = readInput("day04/Day04")
    part1(input).also {
        it.println()
        check(it == 25174)
    }

    part2(input).also {
        it.println()
        check(it == 6420979)
    }
}

private fun String.extractNumbers(): List<Int> = this
    .split(" ")
    .filter { it.isNotBlank() && it.first().isDigit() }
    .map { it.toInt() }

private fun numOfWinningCards(input: List<String>, cardNum: Int): Int {
    //cardNum.println()

    if (cardNum >= input.size) return 0

    var res = 1
    val numberOfMatching = input[cardNum].numberOfMatching()

    for (i in 1..numberOfMatching) {
        res += numOfWinningCards(input, cardNum + i)
    }

    return res
}


private fun String.numberOfMatching(): Int = this
    .split(":")[1]
    .split("|")
    .let { game ->
        check(game.size == 2)
        val myNumbers = game.first()
        val winningNumbers = game[1]

        myNumbers.extractNumbers().intersect(winningNumbers.extractNumbers())
    }
    .size
