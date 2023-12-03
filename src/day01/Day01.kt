package day01

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = input
        .map { calibrationLine ->
            calibrationLine.filter { it.isDigit() }
        }
        .sumOf {
            "${it.first()}${it.last()}".toInt()
        }

    fun part2(input: List<String>): Int = input
        .sumOf { calibrationLine ->
            val firstDigit = calibrationLine
                .findDigitWord()!!
                .replaceDigitWordsWithDigit()

            val lastDigit = calibrationLine
                .foldRight("") { char, acc ->
                    acc.findDigitWord() ?: (char + acc)
                }
                .findDigitWord()!!
                .replaceDigitWordsWithDigit()

            (firstDigit + lastDigit).toInt()
        }


    val testInput = readInput("day01/Day01_part1_test")
    check(part1(testInput) == 142)

    val testInput2 = readInput("day01/Day01_part2_test")
    check(part2(testInput2) == 281)

    val input = readInput("day01/Day01")
    part1(input).println()
    part2(input).println()
}

private fun String.findDigitWord(): String? {
    val regex = """(\d|one|two|three|four|five|six|seven|eight|nine)""".toRegex()

    return regex.find(this)?.value
}

private fun String.replaceDigitWordsWithDigit(): String = this
    .replace("one", "1")
    .replace("two", "2")
    .replace("three", "3")
    .replace("four", "4")
    .replace("five", "5")
    .replace("six", "6")
    .replace("seven", "7")
    .replace("eight", "8")
    .replace("nine", "9")