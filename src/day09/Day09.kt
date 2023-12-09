package day09

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val testInput = Path("src/day09/Day09_test.txt")
    val input = Path("src/day09/Day09.txt")

    println("Part 1:")
    testInput.part1().also { check(it == 114L) }
    input.part1().also { check(it == 1953784198L) }

    println("Part 2:")

    testInput.part2().also { check(it == 2L) }
    input.part2().also { check(it == 957L) }
}

private fun Path.part2() = this.readLines()
    .sumOf { numberRow ->
        numberRow
            .split(" ")
            .map { it.toLong() }
            .buildDiffTree()
            .getPreviousInSequence()
    }
    .also { println("Result: $it") }

private fun List<List<Long>>.getPreviousInSequence(): Long = this
    .first()
    .let { currentRow ->
        if (currentRow.isConstantRow()) {
            currentRow.first()
        } else {
            currentRow.first() - this.drop(1).getPreviousInSequence()
        }
    }

private fun Path.part1() = this.readLines()
    .sumOf { numberRow ->
        numberRow
            .split(" ")
            .map { it.toLong() }
            .nextInSequence()
    }
    .also { println("Result: $it") }

private fun List<Long>.nextInSequence(): Long = this
    .buildDiffTree()
    .sumOf { it.last() }

private fun List<Long>.buildDiffTree(): List<List<Long>> {
    val diffs = mutableListOf(this)

    while (!diffs.last().isConstantRow()) {
        diffs
            .last()
            .windowed(2, 1)
            .fold(mutableListOf()) { acc: MutableList<Long>, longs: List<Long> ->
                acc.apply { this.add(longs.last() - longs.first()) }
            }.also { diff -> diffs.add(diff) }
    }

    return diffs
}

private fun List<Long>.isConstantRow(): Boolean = this.distinct().size == 1