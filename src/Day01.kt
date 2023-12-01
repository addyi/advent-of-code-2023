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
            val regex = """(one|two|three|four|five|six|seven|eight|nine|\d)""".toRegex()
            val matchResults = regex.findAll(calibrationLine)
            val firstDigit = matchResults.first().value.replaceDigitWordsWithDigit()
            val lastDigit = matchResults.last().value.replaceDigitWordsWithDigit()
            (firstDigit + lastDigit).toInt()
        }


    val testInput = readInput("Day01_part1_test")
    check(part1(testInput) == 142)

    val testInput2 = readInput("Day01_part2_test")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println() // Wrong result ¯\_(ツ)_/¯
}