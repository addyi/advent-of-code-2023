fun main() {
    fun part2(input: List<String>): Int = input
        .findGears()
        .sumOf { (rowIndex: Int, columnIndex: Int) ->
            adjacentNumbers(input, rowIndex, columnIndex)
        }


    val testInput = readInput("Day03_test")
    part2(testInput).also {
        it.println()
        check(it == 467835)
    }

    val input = readInput("Day03")
    part2(input).println()
}

private fun List<String>.findGears(): List<Pair<Int, Int>> = this
    .mapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { columnIndex, symbolCandidate ->
            if (symbolCandidate == '*') {
                rowIndex to columnIndex
            } else {
                null
            }
        }
    }
    .flatten()

private fun adjacentNumbers(input: List<String>, rowIndex: Int, columnIndex: Int): Int {
    val adjacentNumbersCandidates = emptyList<String>().toMutableList()

    // numbers in row above
    if (rowIndex > 0) {
        if (input[rowIndex - 1][columnIndex].isDigit()) {
            adjacentNumbersCandidates += numAtCoordinate(input, rowIndex - 1, columnIndex)
        } else {
            adjacentNumbersCandidates += numBeforeCoordinate(input, rowIndex - 1, columnIndex)
            adjacentNumbersCandidates += numAfterCoordinate(input, rowIndex - 1, columnIndex)
        }
    }

    // numbers in same row
    adjacentNumbersCandidates += numBeforeCoordinate(input, rowIndex, columnIndex)
    adjacentNumbersCandidates += numAfterCoordinate(input, rowIndex, columnIndex)

    //numbers in row below
    if (rowIndex < input.size) {
        if (input[rowIndex + 1][columnIndex].isDigit()) {
            adjacentNumbersCandidates += numAtCoordinate(input, rowIndex + 1, columnIndex)
        } else {
            adjacentNumbersCandidates += numBeforeCoordinate(input, rowIndex + 1, columnIndex)
            adjacentNumbersCandidates += numAfterCoordinate(input, rowIndex + 1, columnIndex)
        }
    }

    val adjacentNumbers = adjacentNumbersCandidates
        .filter { it.isNotBlank() }
        .map { it.toInt() }

    return if (adjacentNumbers.size > 2) {
        throw IllegalStateException("More numbers then expected")
    } else if (adjacentNumbers.size == 2) {
        adjacentNumbers.reduce { acc, i -> acc * i }
    } else {
        0
    }
}

private fun numBeforeCoordinate(input: List<String>, rowIndex: Int, columnIndex: Int): String =
    input[rowIndex]
        .take(columnIndex)
        .takeLastWhile { it.isDigit() }

private fun numAfterCoordinate(input: List<String>, rowIndex: Int, columnIndex: Int): String =
    input[rowIndex]
        .drop(columnIndex + 1)
        .takeWhile { it.isDigit() }

private fun numAtCoordinate(input: List<String>, rowIndex: Int, columnIndex: Int): String =
    numBeforeCoordinate(input, rowIndex, columnIndex) +
            input[rowIndex][columnIndex] +
            numAfterCoordinate(input, rowIndex, columnIndex)
