package day09

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTime

fun main() {
    val testInput = Path("src/day09/Day09_test.txt")
    val input = Path("src/day09/Day09.txt")

    testInput.part1().also { check(it == 114L) }

    measureTime {
        input.part1().also { check(it == 1953784198L) }
    }.also { println("Part 1 took $it") }

    testInput.part2().also { check(it == 2L) }


    measureTime {
        input.part2().also { check(it == 957L) }
    }.also { println("Part 2 took $it") }
}

private fun Path.part2() = this.readLines()
    .sumOf { numberRow ->
        numberRow
            .split(" ")
            .map { it.toLong() }
            .previousInSequence()
    }
    .also { println("Result: $it") }

private fun List<Long>.previousInSequence() = this
    .buildDiffTree()
    .let { diffTree ->

        for (diffTreeIndex in (diffTree.size - 1 downTo 0)) {
            val diffRow = diffTree[diffTreeIndex]
            //println("$diffTreeIndex diffRow: $diffRow")

            val prependedDiff = if (diffRow.isConstantRow()) {
                diffRow.plus(diffRow.last())
            } else {
                listOf(diffRow.first() - diffTree[diffTreeIndex + 1].first()).plus(diffRow)
            }
            //println("prependedDiff: $prependedDiff")
            diffTree[diffTreeIndex] = prependedDiff
        }

        diffTree
    }
    .first()
    .first()


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

private fun List<Long>.buildDiffTree(): MutableList<List<Long>> {
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