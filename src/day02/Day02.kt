package day02

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = input
        .map { game -> game.toGame() }
        .fold(0) { acc: Int, game: Game ->
            val isImpossibleGame = game.cubes.any { cube ->
                when (cube) {
                    is Cube.Blue -> cube.numberOfCubes !in 0..14
                    is Cube.Green -> cube.numberOfCubes !in 0..13
                    is Cube.Red -> cube.numberOfCubes !in 0..12
                }
            }

            if (isImpossibleGame) {
                acc
            } else {
                acc + game.gameName.getGameNumber()
            }
        }

    fun part2(input: List<String>): Int = input
        .sumOf { game ->
            val cubes: List<Cube> = game.toGame().cubes

            val fewestNumOfRedCubes = cubes.filterIsInstance<Cube.Red>().maxBy { it.numberOfCubes }.numberOfCubes
            val fewestNumOfBlueCubes = cubes.filterIsInstance<Cube.Blue>().maxBy { it.numberOfCubes }.numberOfCubes
            val fewestNumOfGreenCubes = cubes.filterIsInstance<Cube.Green>().maxBy { it.numberOfCubes }.numberOfCubes

            (fewestNumOfRedCubes * fewestNumOfBlueCubes * fewestNumOfGreenCubes)
        }

    val testInput = readInput("day02/Day02_test")
    part1(testInput).also {
        it.println()
        check(it == 8)
    }

    part2(testInput).also {
        it.println()
        check(it == 2286)
    }

    val input = readInput("day02/Day02")
    part1(input).println()
    part2(input).println()
}

private fun String.getGameNumber(): Int = this.takeLastWhile { it.isDigit() }.toInt()

private data class Game(val gameName: String, val cubes: List<Cube>)

private fun String.toGame(): Game = this
    .split(": ")
    .let { gameInfo ->
        check(gameInfo.size == 2) { "Size of $gameInfo is not 2" }

        gameInfo.first() to gameInfo.last()
    }
    .let { (gameName, subGames) ->
        Game(
            gameName = gameName,
            cubes = subGames
                .split("; ", ", ")
                .map { cube -> cube.toCube() }
        )
    }

private sealed class Cube {
    abstract val numberOfCubes: Int

    data class Red(override val numberOfCubes: Int) : Cube()
    data class Green(override val numberOfCubes: Int) : Cube()
    data class Blue(override val numberOfCubes: Int) : Cube()
}

private fun String.toCube(): Cube = this
    .let { cube ->
        val regex = """(\d*) (red|green|blue)""".toRegex()
        regex.find(cube)
            ?.let { matchResult ->
                val cubeInfo = matchResult.groupValues
                check(cubeInfo.size == 3) { "Cube breakdown failed with $cubeInfo" }

                cubeInfo[1] to cubeInfo[2]// first is always the whole match
            }
            ?.toCube()
            ?: throw IllegalStateException("Cube regex failed for $this")
    }

private fun Pair<String, String>.toCube(): Cube = this
    .let { (numOfCubes, cubeName) ->
        when (cubeName) {
            "red" -> Cube.Red(numOfCubes.toInt())
            "green" -> Cube.Green(numOfCubes.toInt())
            "blue" -> Cube.Blue(numOfCubes.toInt())
            else -> throw IllegalStateException("No match for ($numOfCubes, $cubeName)")
        }
    }