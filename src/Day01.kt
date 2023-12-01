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


    val testInput = readInput("Day01_part1_test")
    check(part1(testInput) == 142)

    val testInput2 = readInput("Day01_part2_test")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}