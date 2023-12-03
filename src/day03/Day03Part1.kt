package day03

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val numbers = emptyList<Int>().toMutableList()
        var numberCandidate = ""
        var isPartNumber = false

        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, colum ->
                if (colum.isDigit()) {
                    numberCandidate += colum

                    if (!isPartNumber) {
                        if (surroundingContainsSymbol(input, rowIndex, columnIndex)) {
                            isPartNumber = true
                        }
                    }
                } else {
                    if (isPartNumber) {
                        numbers += numberCandidate.toInt()
                    }
                    numberCandidate = ""
                    isPartNumber = false
                }
            }
        }

        return numbers.sumOf { it }
    }

    val testInput = readInput("day03/Day03_test")
    part1(testInput).also {
        it.println()
        check(it == 4361)
    }

    val input = readInput("day03/Day03")
    part1(input).println()
    //part2(input).println()
}

private fun Char.isSymbol() = !this.isDigit() && this != '.'

private fun surroundingContainsSymbol(input: List<String>, row: Int, column: Int): Boolean =
    input.getOrNull(row - 1)?.getOrNull(column - 1)?.isSymbol() ?: false // top left
            || input.getOrNull(row - 1)?.getOrNull(column)?.isSymbol() ?: false // top center
            || input.getOrNull(row - 1)?.getOrNull(column + 1)?.isSymbol() ?: false // top right
            || input.getOrNull(row)?.getOrNull(column - 1)?.isSymbol() ?: false // before
            || input.getOrNull(row)?.getOrNull(column + 1)?.isSymbol() ?: false // after
            || input.getOrNull(row + 1)?.getOrNull(column - 1)?.isSymbol() ?: false //bottom left
            || input.getOrNull(row + 1)?.getOrNull(column)?.isSymbol() ?: false // bottom center
            || input.getOrNull(row + 1)?.getOrNull(column + 1)?.isSymbol() ?: false // bottom right
