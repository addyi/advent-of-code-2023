package day05

import println
import readInput
import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Long {
        val seeds = input[0].dropWhile { !it.isDigit() }.split(" ").map { it.toLong() }

        val seedToSoilMap = input.getMapWithName("seed-to-soil map:").getRangeMapping()
        val soilToFertilizerMap = input.getMapWithName("soil-to-fertilizer map:").getRangeMapping()
        val fertilizerToWaterMap = input.getMapWithName("fertilizer-to-water map:").getRangeMapping()
        val waterToLightMap = input.getMapWithName("water-to-light map:").getRangeMapping()
        val lightToTemperatureMap = input.getMapWithName("light-to-temperature map:").getRangeMapping()
        val temperatureToHumidityMap = input.getMapWithName("temperature-to-humidity map:").getRangeMapping()
        val humidityToLocationMap = input.getMapWithName("humidity-to-location map:").getRangeMapping()

        return seeds.minOf {
            seedToSoilMap.getMapped(it).let { soil ->
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


    val testInput = readInput("day05/Day05_test")
    part1(testInput).also {
        it.println()
        check(it == 35L)
    }


    val input = readInput("day05/Day05")
    part1(input).println()
    // part2(input).println()
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
