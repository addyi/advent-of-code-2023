package day06

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val testInput = Path("src/day06/Day06_test.txt")
    val input = Path("src/day06/Day06.txt")

    testInput.part2().also { check(it == 71503L) }
    input.part2().also { check(it == 23654842L) }
}

private fun Path.part2() = this.readLines()
    .let { input ->
        val holdDownTime = input.first().parseInputLine()
        val recordDistance = input.last().parseInputLine()

        LongRange(0, holdDownTime) to recordDistance
    }
    .let { (holdDownTimesAndSpeeds, recordDistance) ->
        val min = holdDownTimesAndSpeeds.first { holdDownTimeAndSpeed ->
            isWinningRace(holdDownTimeAndSpeed, holdDownTimesAndSpeeds.last, recordDistance) != null
        }

        val max = holdDownTimesAndSpeeds.last { holdDownTimeAndSpeed ->
            isWinningRace(holdDownTimeAndSpeed, holdDownTimesAndSpeeds.last, recordDistance) != null
        }

        max - (min - 1)
    }
    .also(::println)

private fun isWinningRace(
    holdDownTimeAndSpeed: Long,
    maxRaceTime: Long,
    recordDistance: Long
): Long? {
    val remainingTravelTime = maxRaceTime - holdDownTimeAndSpeed
    val travelDistance = remainingTravelTime * holdDownTimeAndSpeed
    return if (travelDistance > recordDistance) holdDownTimeAndSpeed else null
}

private fun String.parseInputLine(): Long = this
    .drop(11)
    .filter { it.isDigit() }
    .toLong()