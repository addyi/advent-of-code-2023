package day06

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val testInput = Path("src/day06/Day06_test.txt")
    val input = Path("src/day06/Day06.txt")

    testInput.part1().also { check(it == 288) }
    input.part1().also { check(it == 303600) }

//    measureTime {
//        input.part1()
//    }.also { println("Part 1 took ${it.inWholeSeconds}s") }
}

private fun Path.part1() = this.readLines()
    .let { input ->
        val times = input.first().parseInputLine().map { IntRange(0, it) }
        val recordDistances = input.last().parseInputLine()

        times.zip(recordDistances)
    }
    .map { (timeRange, recordDistance) ->
        timeRange.mapNotNull { holdDownTimeAndSpeed ->
            val remainingTravelTime = timeRange.last - holdDownTimeAndSpeed
            val travelDistance = remainingTravelTime * holdDownTimeAndSpeed
            if (travelDistance > recordDistance) holdDownTimeAndSpeed else null
        }
    }
    .map {
        it.size
    }
    .let {
        it.reduce { acc, i -> acc * i }
    }
    .also(::println)

private fun String.parseInputLine(): List<Int> = this
    .drop(11)
    .split(" ")
    .map { it.trim() }
    .filter { it.isNotBlank() }
    .map { it.toInt() }