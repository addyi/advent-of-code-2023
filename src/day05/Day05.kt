package day05

import println
import readInput
import kotlin.math.absoluteValue
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long {
        val seeds = input[0].dropWhile { !it.isDigit() }.split(" ").map { it.toLong() }

        val convertSeedToLocation: (Long) -> Long = seedToLocationConverter(input)

        return seeds.minOf { convertSeedToLocation(it) }
    }


    fun part2(input: List<String>): Long {
        val seeds = input[0]
            .dropWhile { !it.isDigit() }
            .split(" ")
            .map { it.toLong() }
            .windowed(2, 2)
            .map { LongRange(it[0], (it[0] + it[1]) - 1) }
            .mergeOverlappingRanges()

        val convertSeedToLocation: (Long) -> Long = seedToLocationConverter(input)

        return seeds.minOf { seedRanges ->
            seedRanges.minOf { seed -> convertSeedToLocation(seed) }
        }
    }

    val testInput = readInput("day05/Day05_test")
    part1(testInput).also {
        it.println()
        check(it == 35L)
    }

    part2(testInput).also {
        it.println()
        check(it == 46L)
    }

    val input = readInput("day05/Day05")
    part1(input).also {
        it.println()
        check(it == 1181555926L)
    }

    measureTime {
        part2(input).also {
            it.println()
            check(it == 37806486L)
        }
    }.also { println("Part 2 took ${it.inWholeSeconds}s") } // ~ 2 min
}

private fun List<RangeMapping>.getMapped(id: Long): Long = this
    .find { it.sourceRange.contains(id) }
    ?.let { rangeMap ->
        (rangeMap.destination.first + ((rangeMap.sourceRange.first - id).absoluteValue))
    }
    ?: id

private data class RangeMapping(val sourceRange: LongRange, val destination: LongRange)

private fun List<String>.getRangeMapping(): List<RangeMapping> = this
    .map { mappingLine ->
        mappingLine
            .split(" ")
            .map { it.toLong() }
            .let { mapLine ->
                RangeMapping(
                    sourceRange = LongRange(mapLine[1], (mapLine[1] + mapLine[2]) - 1),
                    destination = LongRange(mapLine[0], (mapLine[0] + mapLine[2]) - 1)
                )
            }
    }

private fun List<String>.getMapWithName(name: String): List<String> = this
    .dropWhile { it != name }
    .drop(1)
    .takeWhile { it.isNotBlank() && it[0].isDigit() }

private fun List<LongRange>.mergeOverlappingRanges(): List<LongRange> {
    val reducedSetOfRanges = mutableListOf<LongRange>()

    var currentRange = this[0]

    for (i in 1 until this.size) {
        val nextRange = this[i]

        currentRange = if (currentRange.last >= nextRange.first) {
            // Merge the ranges
            currentRange.first..maxOf(currentRange.last, nextRange.last)
        } else {
            // Add the current range to the result and move to the next one
            reducedSetOfRanges.add(currentRange)
            nextRange
        }
    }

    // Add the last range
    reducedSetOfRanges.add(currentRange)
    return reducedSetOfRanges
}

private fun seedToLocationConverter(input: List<String>): (Long) -> Long {
    val seedToSoilMap = input.getMapWithName("seed-to-soil map:").getRangeMapping()
    val soilToFertilizerMap = input.getMapWithName("soil-to-fertilizer map:").getRangeMapping()
    val fertilizerToWaterMap = input.getMapWithName("fertilizer-to-water map:").getRangeMapping()
    val waterToLightMap = input.getMapWithName("water-to-light map:").getRangeMapping()
    val lightToTemperatureMap = input.getMapWithName("light-to-temperature map:").getRangeMapping()
    val temperatureToHumidityMap = input.getMapWithName("temperature-to-humidity map:").getRangeMapping()
    val humidityToLocationMap = input.getMapWithName("humidity-to-location map:").getRangeMapping()

    return { seed ->
        seedToSoilMap.getMapped(seed).let { soil ->
            soilToFertilizerMap.getMapped(soil).let { fertilizer ->
                fertilizerToWaterMap.getMapped(fertilizer).let { water ->
                    waterToLightMap.getMapped(water).let { light ->
                        lightToTemperatureMap.getMapped(light).let { temperature ->
                            temperatureToHumidityMap.getMapped(temperature).let { humidity ->
                                humidityToLocationMap.getMapped(humidity)
                            }
                        }
                    }
                }
            }
        }
    }
}
